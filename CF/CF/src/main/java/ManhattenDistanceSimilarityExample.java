import java.io.File;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;

public class ManhattenDistanceSimilarityExample {

	public static void main(String[] args) {
	try {
		BasicConfigurator.configure();
		DataModel model = new FileDataModel(new File("E:\\documents\\Documents\\KLTN2019\\db\\MDist1.txt"));
		CityBlockSimilarity similarity = new CityBlockSimilarity(model);
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1,similarity, model);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
		  for (int i = 1; i < 5; i++) {
			// The First argument is the userID and the Second parameter is 'HOW MANY'
      		List<RecommendedItem> recommendations = recommender.recommend(i, 3);      
			
     		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation);
     		}
	     	System.out.println();
		}
		} catch (Exception e) {
			System.out.println("Exception occured !");
		}

	}

}