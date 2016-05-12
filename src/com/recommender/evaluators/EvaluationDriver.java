package com.recommender.evaluators;

/**
 * Created by arajawat on 5/11/2016.
 */
public class EvaluationDriver {

    private int typeOfDataSet;
    private int typeOfRecommender;
    private int typeOfEvaluation;
    private int numOfRecommendations;

    public EvaluationDriver(int typeOfDataSet, int typeOfRecommender, int typeOfEvaluation
                            , int numOfRecommendations){
        this.typeOfDataSet = typeOfDataSet;
        this.typeOfRecommender = typeOfRecommender;
        this.typeOfEvaluation = typeOfEvaluation;
        this.numOfRecommendations = numOfRecommendations;
    }

    public void evaluate() throws Exception{

        String filePath = getDataModelPath(typeOfDataSet);
        String similarity = getSimilarityType(typeOfRecommender);

        if(typeOfEvaluation == 1){
            IRBasedEvaluator irEvaluator = new IRBasedEvaluator(similarity, filePath, numOfRecommendations);
            irEvaluator.evaluate();
        }
        else if(typeOfEvaluation == 2){
            PredictionBasedEvaluator predictionEvaluator = new PredictionBasedEvaluator(similarity, filePath);
            predictionEvaluator.evaluate();
        }
    }

    private String getDataModelPath(int choice){
        String filePath = "";
        if(choice == 1){
            filePath = "/user/user01/EventsRecommendationProject/out/data/usereventpreferences/part-r-00000";
        }
        else if(choice == 2){
            filePath = "/user/user01/EventsRecommendationProject/out/data/usereventattended/part-r-00000";
        }
        return filePath;
    }

    private String getSimilarityType(int choice){
        String similarity = "";
        if(choice == 1){
            similarity = "TanimotoCoefficientSimilarity";
        }
        else if(choice == 2){
            similarity = "LogLikelihoodSimilarity";
        }
        return similarity;
    }

}

