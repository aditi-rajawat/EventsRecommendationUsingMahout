package com.recommender.main;

import com.recommender.datafilters.UsersRecentAttendedEventsDriver;
import com.recommender.datafilters.UsersRecentHistoryDriver;
import com.recommender.datagenerators.EventsContentAttributesDriver;
import com.recommender.datagenerators.UsersEventsAttendedDriver;
import com.recommender.datagenerators.UsersEventsPreferencesDriver;
import com.recommender.evaluators.EvaluationDriver;
import com.recommender.itemrecommenders.ItemBasedRecommender;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        int choice = -1, similarityMetric = -1, successCode = -1, comparison = -1, numOfRecos = -1;
        String repeat = "N";
        String dataGenerator = null;
        String[] args1 = new String[2];
        String[] args2 = new String[3];
        String userId;
        Scanner sc = new Scanner(System.in);

        System.out.println("*********************************************************************************************");
        System.out.println("**********************************  EVENT'S RECOMMENDATION **********************************");
        System.out.println("*********************************************************************************************\n");
        System.out.println("We will follow the following steps:\n");
        System.out.println("STEP# 1  :  Data Modelling\n");
        System.out.println("STEP# 2  :  Item Based Recommendations Generation\n");
        System.out.println("STEP#  3  :  Recommender Evaluation\n\n");
        System.out.println("********************************** DATA MODELLING: START **********************************\n");
        System.out.println("Make a choice for the type of the training dataset and press Enter\n");
        System.out.println("1  :  Users , Events , Preferences");
        System.out.println("2  :  Users , Events , Attended");
        System.out.println("3  :  Events  ,  100 Stem Words");
        System.out.println("0  :  No Choice\n");
        choice = sc.nextInt();
        sc.nextLine();
        if (choice == 1 || choice == 2 || choice == 3) {    // Ensuring that the user has entered an input choice
            dataGenerator = selectDataGenerator(choice);
            Configuration hadoopConf = new Configuration();
            if (dataGenerator.equals("UsersEventsPreferencesDriver")) {
                args1[0] = "/user/user01/EventsRecommendationProject/data/train";
                args1[1] = "/user/user01/EventsRecommendationProject/out/data/usereventpreferences";
                successCode = ToolRunner.run(hadoopConf, new UsersEventsPreferencesDriver(), args1);
            } else if (dataGenerator.equals("UsersEventsAttendedDriver")) {
                args1[0] = "/user/user01/EventsRecommendationProject/data/attendees";
                args1[1] = "/user/user01/EventsRecommendationProject/out/data/usereventattended";
                successCode = ToolRunner.run(hadoopConf, new UsersEventsAttendedDriver(), args1);
            } else if(dataGenerator.equals("EventsContentAttributesDriver")){
                args1[0] = "/user/user01/EventsRecommendationProject/data/events";
                args1[1] = "/user/user01/EventsRecommendationProject/out/data/eventsattributes";
                successCode = ToolRunner.run(hadoopConf, new EventsContentAttributesDriver(), args1);
            }else {
                shutDown();
            }

            if (successCode == 1) {
                System.err.println("FATAL: Data generation job on hadoop cluster failed..exiting the system");
                System.exit(-1);
            } else if (successCode == 0) {
                System.out.println("********************************** DATA MODELLING: COMPLETED!! **********************************\n");
                System.out.println("******************************* ITEM BASED RECOMMENDATIONS: START ********************************\n");
                System.out.println("Enter a user Id and press ENTER\n");
                userId = sc.nextLine();
                System.out.println("\n HELLO " + userId + " FROM MAHOUT...\n");

                System.out.println("********************************** EXTRACTING USER'S HISTORY ************************************\n");
                successCode = -1;
                if(choice == 1 || choice == 3){
                    args2[0] = "/user/user01/EventsRecommendationProject/data/train";
                    args2[1] = "/user/user01/EventsRecommendationProject/out/data/usershistory";
                    args2[2] = userId;
                    successCode = ToolRunner.run(hadoopConf, new UsersRecentHistoryDriver(), args2);
                }
                else if(choice == 2){
                    args2[0] = "/user/user01/EventsRecommendationProject/data/attendees";
                    args2[1] = "/user/user01/EventsRecommendationProject/out/data/usersattendedhistory";
                    args2[2] = userId;
                    successCode = ToolRunner.run(hadoopConf, new UsersRecentAttendedEventsDriver(), args2);
                }

                if (successCode == 1) {
                    System.err.println("FATAL: Fetch user's history job on hadoop cluster failed..exiting the system");
                    System.exit(-1);
                }
                else if(successCode == 0){
                    System.out.println("\n********************************** CHOOSE SIMILARITY METRIC ************************************\n");
                    System.out.println("Make a choice for the type of the similarity metric to be used and press Enter\n");
                    System.out.println("Note: If you choose events-stem words as the training data-set then choose 3\n");
                    System.out.println("1  :  Tanimoto Coefficient");
                    System.out.println("2  :  Log Likelihood");
                    //System.out.println("3  :  Pearson Correlation");
                    System.out.println("0  :  No Choice\n");
                    similarityMetric = sc.nextInt();
                    sc.nextLine();

                    /*
                    if(choice == 3 && similarityMetric != 3){
                        System.out.println("Invalid similarity metric selection...only option 3 is allowed for the events-stem words training dataset\n");
                        shutDown();
                    } */
                    if (similarityMetric == 1 || similarityMetric == 2) {
                        ItemBasedRecommender recommender = new ItemBasedRecommender(userId, similarityMetric, choice);
                        recommender.recommend();
                    } else {
                        shutDown();
                    }

                    System.out.println("*************************** ITEM BASED RECOMMENDATIONS: COMPLETED!! ***************************\n");
                    System.out.println("******************************* RECOMMENDER EVALUATION : START *********************************\n");
                    do {
                        System.out.println("Choose the type of evaluation and press Enter\n");
                        System.out.println("1  :  IR Based Measures");
                        System.out.println("2  :  Prediction Based Measures");
                        System.out.println("0  :  No Choice\n");
                        comparison = sc.nextInt();
                        sc.nextLine();

                        if (comparison == 1) {
                            System.out.println("Enter the no. of recommendations to be considered and press Enter\n");
                            numOfRecos = sc.nextInt();
                            sc.nextLine();
                        }

                        if (comparison == 1 || comparison == 2) {
                            EvaluationDriver evaluationDriver = new EvaluationDriver(choice, similarityMetric, comparison, numOfRecos);
                            evaluationDriver.evaluate();
                        } else {
                            shutDown();
                        }

                        System.out.println("Do you want to try the other evaluator? Enter (Y/N) and press Enter\n");
                        repeat = sc.nextLine();
                    }while (repeat.equalsIgnoreCase("Y"));

                    System.out.println("**************************** RECOMMENDER EVALUATION : COMPLETED!! *******************************\n");
                }
            }
        } else {
            shutDown();
        }
    }

    private static String selectDataGenerator(int choice) {
        if (choice == 1) {
            return "UsersEventsPreferencesDriver";
        } else if (choice == 2) {
            return "UsersEventsAttendedDriver";
        } else if (choice == 3){
            return "EventsContentAttributesDriver";
        }else {
            return null;
        }
    }

    private static void shutDown() {
        System.out.println(" No or invalid choice made...shutting down the system\n");
        System.out.println("System exit..");
        System.exit(-1);
    }

}


/*
  private static Object createInstance(String className){
        try{

            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();

        }catch (ClassNotFoundException ex){
            System.err.println("FATAL : Class not found!!");
        }catch (NoSuchMethodException ex){
            System.err.println("FATAL : Constructor not found!!");
        }catch (Exception ex){
            System.err.println("FATAL : Cannot instantiate an object of the class!!");
        }

        return null;
    }
 */
