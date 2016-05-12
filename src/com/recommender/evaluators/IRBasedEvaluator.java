package com.recommender.evaluators;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.File;

/**
 * Created by arajawat on 5/11/2016.
 */
public class IRBasedEvaluator {

    private String similarity;
    private String filePath;
    private int numOfRecommendations;

    public IRBasedEvaluator(String similarity, String filePath,
                            int numOfRecommendations){

        this.similarity = similarity;
        this.filePath = filePath;
        this.numOfRecommendations = numOfRecommendations;
    }

    public void evaluate() throws Exception{

        if(filePath != null){
            DataModel dataModel = new FileDataModel(new File(filePath));
            RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
            RecommenderBuilder builder = null;

            if(similarity.equals("LogLikelihoodSimilarity")){
                builder = new RecommenderBuilder() {
                    @Override
                    public Recommender buildRecommender(DataModel model) throws TasteException {
                        ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
                        return new GenericBooleanPrefItemBasedRecommender(model, itemSimilarity);
                    }
                };
            }
            else if(similarity.equals("TanimotoCoefficientSimilarity")){
                builder = new RecommenderBuilder() {
                    @Override
                    public Recommender buildRecommender(DataModel model) throws TasteException {
                        TanimotoCoefficientSimilarity tcSimilarity = new TanimotoCoefficientSimilarity(model);
                        return new GenericBooleanPrefItemBasedRecommender(model, tcSimilarity);
                    }
                };
            }

            IRStatistics statistics = evaluator.evaluate(builder, null, dataModel, null, numOfRecommendations,
                                    GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);

            System.out.println("************************* IR BASED EVALUATIONS ****************************\n");
            System.out.println("***************************************************************************\n");
            System.out.println("PRECISION  :  "+ statistics.getPrecision());
            System.out.println("RECALL  :  "+ statistics.getRecall());
            System.out.println("***************************************************************************\n");

        }

    }

}
