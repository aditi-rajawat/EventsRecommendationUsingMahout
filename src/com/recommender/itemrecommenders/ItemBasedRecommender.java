package com.recommender.itemrecommenders;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by arajawat on 5/7/2016.
 */
public class ItemBasedRecommender {

    private String userId;
    /*
        1 : Tanimoto Coefficient
        2 : Log Likelihood
     */
    private int similarityMetric;
    private int dataChoice;

    public ItemBasedRecommender(String userId, int similarityMetric, int dataChoice){
        this.userId = userId;
        this.similarityMetric = similarityMetric;
        this.dataChoice = dataChoice;
    }

    public void recommend() throws Exception{
        String line = null;
        List<Long> itemIds = new ArrayList<>();
        Map<Long, List<RecommendedItem>> map = new HashMap<>();
        FileReader fileReader = null;

        if(dataChoice == 1)
            fileReader = new FileReader(new File("/user/user01/EventsRecommendationProject/out/data/usershistory/part-r-00000"));
        else if(dataChoice == 2)
            fileReader = new FileReader(new File("/user/user01/EventsRecommendationProject/out/data/usersattendedhistory/part-r-00000"));

        if(fileReader != null){
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null){
                String[] tokens = line.split("\\t");
                if(tokens != null){
                    if(tokens.length == 2){
                        String[] items = tokens[1].split(",");
                        for(String eachItem: items){
                            itemIds.add(Long.parseLong(eachItem));
                        }
                    }
                }
            }

            if(similarityMetric == 1){
                map = TanimotoCoefficientRecommender.createRecommendations(itemIds, dataChoice);
            }
            else if(similarityMetric == 2){
                map = LogLikelihoodRecommender.createRecommendations(itemIds, dataChoice);
            }

            if(!map.isEmpty()){
                Set<Long> keys = map.keySet();
                if(keys.size()>0){
                    System.out.println("************************* RECOMMENDATIONS ****************************\n");
                    for(Long itemId: keys){
                        List<RecommendedItem> recommendations = map.get(itemId);
                        System.out.println("******************************************************************\n");
                        System.out.println("Since you showed interest in the EVENT "+ itemId + ", you may also like:\n");
                        for(RecommendedItem eachReco : recommendations){
                            System.out.println("Event ID  :  "+ eachReco.getItemID() + " , Similarity Value  :  "+ eachReco.getValue());
                        }
                        System.out.println("******************************************************************\n");
                    }
                }
            }

        }

    }

}
