import java.io.File;
import java.util.List;
 
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

import untils.MyUntils;

public class UserBasedRecommender {
	 public static void main(String[] args) throws Exception {
	        RandomUtils.useTestSeed(); // to randomize the evaluation result        
	        String fileName = MyUntils.FileLocation();
			DataModel model = new FileDataModel(new File(fileName));
	 
	        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
	            public Recommender buildRecommender(DataModel model) throws TasteException {                
	                
	                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
	                //SpearmanCorrelationSimilarity similarity = new SpearmanCorrelationSimilarity(model);
	        
	                // neighborhood size = 100
	                UserNeighborhood neighborhood = new NearestNUserNeighborhood (100, similarity, model);                
	                return new GenericUserBasedRecommender(model, neighborhood, similarity);                
	            }
	        };
	 
	    // Recommend certain number of items for a particular user
        // Here, recommending 5 items to user_id = 9
	    Recommender recommender = recommenderBuilder.buildRecommender(model);
	    List<RecommendedItem> recomendations = recommender.recommend(9, 5);
	    for (RecommendedItem recommendedItem : recomendations) {
	        System.out.println(recommendedItem);    
	    }
	    
	RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
	double score = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
	System.out.println("RMSE: " + score);
	
	RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
	IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, model, null, 10, 4, 0.7); // evaluate precision recall at 10
	    
		System.out.println("Precision: " + stats.getPrecision());
		System.out.println("Recall: " + stats.getRecall());
		System.out.println("F1 Score: " + stats.getF1Measure());               
	}
}

