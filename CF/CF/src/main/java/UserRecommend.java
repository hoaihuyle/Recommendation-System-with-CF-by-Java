import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import untils.MyUntils;

public class UserRecommend {

	public static void main(String[] args) throws IOException, TasteException {
		// TODO Auto-generated method stub
		String fileName = MyUntils.FileLocation();
		DataModel model = new FileDataModel(new File(fileName));
		
		UserSimilarity similarity = 
				new UncenteredCosineSimilarity(model);
		
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, model);
		
		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
		for (int i = 1; i < model.getNumUsers()+1; i++) {
			// The First argument is the userID and the Second parameter is 'HOW MANY'
			List<RecommendedItem> recommendations = recommender.recommend(i, 3);     
			System.out.print("Recommendation for USER ID ["+i+"]:");
			for (RecommendedItem recommendation : recommendations) {
				System.out.print("\t"+recommendation);
			}
			System.out.println();
		}
		
		
	}

}
