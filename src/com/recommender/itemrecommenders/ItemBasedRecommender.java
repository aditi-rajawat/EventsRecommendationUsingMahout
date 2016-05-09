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

    public ItemBasedRecommender(String userId, int similarityMetric){
        this.userId = userId;
        this.similarityMetric = similarityMetric;
    }

    public void recommend() throws Exception{
        String line = null;
        List<Long> itemIds = new ArrayList<>();
        Map<Long, List<RecommendedItem>> map = new HashMap<>();
        FileReader fileReader = new FileReader(new File("/user/user01/EventsRecommendationProject/out/data/usershistory/part-r-00000"));
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
            map = TanimotoCoefficientRecommender.createRecommendations(itemIds);
        }
        else if(similarityMetric == 2){
            map = LogLikelihoodRecommender.createRecommendations(itemIds);
        }

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
