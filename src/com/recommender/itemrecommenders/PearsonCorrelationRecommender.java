package com.recommender.itemrecommenders;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arajawat on 5/10/2016.
 */
public class PearsonCorrelationRecommender {

    public static Map<Long, List<RecommendedItem>> createRecommendations(List<Long> itemIds, int dataChoice) throws Exception{

        Map<Long, List<RecommendedItem>> map = new HashMap<Long, List<RecommendedItem>>();
        File file = null;

        if(dataChoice == 1)
            file = new File("/user/user01/EventsRecommendationProject/out/data/usereventpreferences/part-r-00000");
        else if(dataChoice == 2)
            file = new File("/user/user01/EventsRecommendationProject/out/data/usereventattended/part-r-00000");
        else if(dataChoice == 3)
            file = new File("/user/user01/EventsRecommendationProject/out/data/eventsattributes/part-r-00000");

        if(file != null){

            if(dataChoice == 1 || dataChoice == 2){
                DataModel dataModel = new FileDataModel(file);

                ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);

                GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, similarity);

                for(Long eachItemId: itemIds){
                    List<RecommendedItem> recommendations = recommender.mostSimilarItems(eachItemId.longValue(), 5);
                    map.put(eachItemId, recommendations);
                }
            }
            else if(dataChoice == 3){
//                FileReader fileReader = new FileReader(file);
//                BufferedReader bufferedReader = new BufferedReader(fileReader);
//                String line = null;
//
//                FastByIDMap<PreferenceArray> data = new FastByIDMap<>();
//
//                while((line = bufferedReader.readLine()) != null){
//                    String[] tokens = line.split(",");
//                    String eventId = tokens[0];
//                    int numOfPrefs = tokens.length - 1; // as first token contains the event Id
//
//                    PreferenceArray preferenceArray = new GenericUserPreferenceArray(numOfPrefs);
//                    for(int i=0; i<numOfPrefs; i++){
//                        preferenceArray.set(i, new GenericPreference(Long.parseLong(eventId), i, Float.parseFloat(tokens[i+1])));
//                    }
//                    data.put(Long.parseLong(eventId), preferenceArray);
//                }

                DataModel model = new FileDataModel(file);

                UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(10, userSimilarity, model);
                Recommender recommender = new GenericUserBasedRecommender(model, userNeighborhood, userSimilarity);

                for(Long id: itemIds){
                    List<RecommendedItem> recommendedItems = recommender.recommend(id.longValue(), 5);
                    map.put(id, recommendedItems);
                }
            }

        }

        return map;
    }

}
