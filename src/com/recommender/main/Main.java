package com.recommender.main;

import com.recommender.datafilters.UsersRecentAttendedEventsDriver;
import com.recommender.datafilters.UsersRecentHistoryDriver;
import com.recommender.datagenerators.UsersEventsAttendedDriver;
import com.recommender.datagenerators.UsersEventsPreferencesDriver;
import com.recommender.itemrecommenders.ItemBasedRecommender;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        int choice = -1, similarityMetric = -1, successCode = -1;
        String dataGenerator = null;
        String[] args1 = new String[2];
        String[] args2 = new String[3];
        String userId;
        Scanner sc = new Scanner(System.in);

        System.out.println("********************************************************");
        System.out.println("***************  EVENT'S RECOMMENDATION ****************");
        System.out.println("********************************************************\n");
        System.out.println("******************** DATA GENERATION: START *******************\n");
        System.out.println("Make a choice for the type of the training dataset and press Enter\n");
        System.out.println("1  :  Users , Events , Preferences");
        System.out.println("2  :  Users , Events , Attended");
        System.out.println("0  :  No Choice\n");
        choice = sc.nextInt();
        sc.nextLine();
        if (choice == 1 || choice == 2) {    // Ensuring that the user has entered an input choice
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
            } else {
                shutDown();
            }

            if (successCode == 1) {
                System.err.println("FATAL: Data generation job on hadoop cluster failed..exiting the system");
                System.exit(-1);
            } else if (successCode == 0) {
                System.out.println("******************** DATA GENERATION: COMPLETED!! *******************\n");
                System.out.println("******************** ITEM BASED RECOMMENDATIONS: START *******************\n");
                System.out.println("Enter a user Id and press ENTER\n");
                userId = sc.nextLine();
                System.out.println("\n Hello " + userId + " from Mahout...\n");

                System.out.println("Running hadoop job to fetch user's recent history of the events");
                successCode = -1;
                if(choice == 1){
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
                    System.out.println("Make a choice for the type of the similarity metric to be used and press Enter\n");
                    System.out.println("1  :  Tanimoto Coefficient");
                    System.out.println("2  :  Log Likelihood");
                    System.out.println("0  :  No Choice\n");
                    similarityMetric = sc.nextInt();
                    sc.nextLine();

                    if (similarityMetric == 1 || similarityMetric == 2) {
                        ItemBasedRecommender recommender = new ItemBasedRecommender(userId, similarityMetric, choice);
                        recommender.recommend();
                    } else {
                        shutDown();
                    }
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
        } else {
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
