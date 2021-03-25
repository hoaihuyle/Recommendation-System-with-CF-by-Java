import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity.ItemItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import untils.MyUntils;

/**
 * Calculate similarity mark between two item
 * @author Admin
 *
 */
public class ItemRecommend {

	public static void main(String[] args) {
		try {
			BasicConfigurator.configure();
			String fileName = MyUntils.FileLocation();
			DataModel model = new FileDataModel(new File(fileName));
			
			ItemSimilarity sim = new UncenteredCosineSimilarity(model);
			
			GenericItemBasedRecommender  recommender = new GenericItemBasedRecommender(model, sim);
			
			// Construct the list of pre-computed correlations
			Collection<GenericItemSimilarity.ItemItemSimilarity> correlations =  new ArrayList<GenericItemSimilarity.ItemItemSimilarity>();

			for(LongPrimitiveIterator items = model.getItemIDs();items.hasNext();) {
				long itemId = items.nextLong();
				
				List<RecommendedItem>recommendations = recommender.mostSimilarItems(itemId, 10);
//				System.out.println("Recommendation for Item ID "+itemId);
				for(RecommendedItem recommendation : recommendations) {
//					Add item to save in a list of pre-computed correlations
					correlations.add(new GenericItemSimilarity.ItemItemSimilarity(itemId, recommendation.getItemID(), recommendation.getValue()));
					
				}
			}
			
			sim = new GenericItemSimilarity(correlations);
			recommender =new GenericItemBasedRecommender(model, sim);
			for (ItemItemSimilarity itemItemSimilarity : correlations) {
				//HOW Check users was rated itemItemSimilarity.getItemID1 ?
				
				System.out.println(itemItemSimilarity);
			}
		} catch (IOException e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		} catch (TasteException e) {
			System.out.println("There was a Taste Exception");
			e.printStackTrace();
		}
		

	}
	

}
