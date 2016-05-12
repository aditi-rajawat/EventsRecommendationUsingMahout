package com.recommender.evaluators;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
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
public class PredictionBasedEvaluator {

    private String similarity;
    private String filePath;

    public PredictionBasedEvaluator(String similarity, String filePath){
        this.similarity = similarity;
        this.filePath = filePath;
    }

    public void evaluate() throws Exception{

        if(filePath != null){
            DataModel dataModel = new FileDataModel(new File(filePath));
            RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
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

            double rms = evaluator.evaluate(builder, null, dataModel, 0.7, 1.0);

            System.out.println("************************* PREDICTION BASED EVALUATIONS ****************************\n");
            System.out.println("***************************************************************************\n");
            System.out.println("RMS difference between the predicted and actual preference for events  :  " + rms);
            System.out.println("***************************************************************************\n");

        }
    }
}
