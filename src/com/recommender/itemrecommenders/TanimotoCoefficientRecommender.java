package com.recommender.itemrecommenders;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arajawat on 5/8/2016.
 */
public class TanimotoCoefficientRecommender {

    public static Map<Long, List<RecommendedItem>> createRecommendations(List<Long> itemIds) throws Exception{

        Map<Long, List<RecommendedItem>> map = new HashMap<Long, List<RecommendedItem>>();

        File file = new File("/user/user01/EventsRecommendationProject/out/data/usereventpreferences/part-r-00000");
        DataModel dataModel = new FileDataModel(file);

        TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);

        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, similarity);

        for(Long eachItemId: itemIds){
            List<RecommendedItem> recommendations = recommender.mostSimilarItems(eachItemId.longValue(), 5);
            map.put(eachItemId, recommendations);
        }
        return map;
    }
}
