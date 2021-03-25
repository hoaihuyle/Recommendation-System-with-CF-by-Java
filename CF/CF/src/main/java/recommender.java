import java.io.IOException;


public class recommender {

	static ReadMatrix m100 = new ReadMatrix();
	private static final int numItem = 3;
	private static final int KNEAREST = 3;

	/**
	 * Average User
	 * @param matrix Average Column
	 * @return array with length = matrix[0].length
	 */
	public static double[] arrAverageUser(double [][] matrix) {
		
		double arr[] = new double[matrix[0].length]; 
		int j=0;
		while (j<matrix[0].length) {
			double sum=0;
			int i=0;
			int count=0;
			while(i<matrix.length) {
				if(matrix[i][j]!=-1) {
					sum=sum + matrix[i][j];
					count++;
				}
				i++;
			}
			try {
				arr[j]=(double) (Math.round((sum/count)*100.0)/100.0);
			} catch (Exception e) {
				// TODO: handle exception
				arr[j]=0;
			}
			
			j++;
		}
		return arr;
	}
	
	/**
	 * Normalized Matrix by get original matrix minus value in array AverageUser
	 * @param matrix
	 * @param arr
	 * @return matrix 2d
	 */
	public static double[][] normalizedMatrix(double [][] matrix, double [] arrAverageUser){
		
		double[][] normatrix = new double[matrix.length][matrix[0].length];//[item][user]
		
		for (int i = 0; i < normatrix[0].length; i++) {
			for (int j = 0; j < normatrix.length; j++) {
				if(matrix[j][i]!=-1)
				normatrix[j][i]= matrix[j][i] - arrAverageUser[i];
				else normatrix[j][i] = 0;
			}
		}
		return normatrix;
	}
	
	/**
	 * Convert normalizedMatrix to originalMatrix with full element was predicted
	 * @param matrix
	 * @param fullMatrix 
	 * @param arr
	 * @return matrix 2d
	 */
	public static void convertMatrix(double [][] matrix, double[][] fullMatrix, double [] arrAverageUser){
				
		for (int i = 0; i < matrix[0].length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				fullMatrix[j][i]=fullMatrix[j][i]+arrAverageUser[i];
			}
		}
	}
	
	/**
	 * compute the similarity between user to user
	 * Create a matrix with length userxuser to save data
	 * @param normalizedMatrix
	 * @return a matrix 2d userxuser
	 */
	public static double[][] similarityMatrix(double [][] normalizedMatrix){
		
		double[][] similarityMatrix = new double[normalizedMatrix[0].length][normalizedMatrix[0].length];//[item][user]
		
		//Initialize the matrix similarity between user to user
		for (int i = 0; i < similarityMatrix.length; i++) {
			for (int j = 0; j < similarityMatrix.length; j++) {
				if(i==j) {similarityMatrix[i][j] =1;}
				else {
					//Initialize the vector have length is the total rows in matrix normalizedMatrix
					double [] arrVec1 = new double[ normalizedMatrix.length];
					double [] arrVec2 = new double[ normalizedMatrix.length];
					
					for (int j2 = 0; j2 < normalizedMatrix.length; j2++) {
						arrVec1[j2] =  normalizedMatrix[j2][i];
						arrVec2[j2] =  normalizedMatrix[j2][j];
					}
					//compute similarity by cosine function
					double multipVec =0;
					double sizeVec1 =0;
					double sizeVec2 =0;

					for (int l = 0; l < normalizedMatrix.length; l++) {										
						multipVec= arrVec1[l]*arrVec2[l]+multipVec;
						sizeVec1= (double) (Math.pow(arrVec1[l], 2) + sizeVec1);
						sizeVec2= (double) (Math.pow(arrVec2[l], 2) + sizeVec2);
					}
					similarityMatrix[i][j] = (double) (Math.round(multipVec/(Math.sqrt(sizeVec1)*Math.sqrt(sizeVec2))*100.0)/100.0);
				}
			
			}
		}
		return similarityMatrix;
	}
			
	/**
	 * Find all users who rated item x and sorted by Similarity Matrix, Called by predictedRatingByKNN function
	 * @param normalizedMatrix
	 * @param similarityMatrix
	 * @param item
	 * @param user
	 * @return array container index USER WHO rated on a item
	 */
	public static int[] arrUserRateIndex(double [][] normalizedMatrix, double [][] similarityMatrix, int item, int user){
		
		int count=0;
		
		for (double f : normalizedMatrix[item]) {
			if(f!=0) { count++;  }
		}
		
		int [] arr = new int[count];
		//users who rated item
		int i=0;
		int j=0;
		
		for (double f : normalizedMatrix[item]) {
			if(f!=0) {arr[j]=i; j++;}
			i++;
		}
		SortDecsIndex(arr, similarityMatrix, user); //Sort desc array by Similarity value
		return arr;
	}
		
	/**
	 * Sort Array DECS
	 * @param array
	 */
	public static void SortDecs(double[] array) {
		double temp =array[0];
		for (int i = 1; i < array.length; i++) {
			for (int j = i; j > 0; j--) {
				if (array[j] > array [j - 1]) {
					temp = array[j];
					array[j] = array[j - 1];
					array[j - 1] = temp;
				}
			}
		}
	}
	
	/**
	 * Sort Array change Index DECS
	 * @param array
	 */
	public static void SortDecsIndex(int[] array, double[][]similarityMatrix, int user) {
		int temp =array[0];
		for (int i = 1; i < array.length; i++) {
			for (int j = i; j > 0; j--) {
				if (similarityMatrix[user][array[j]] > similarityMatrix[user][array[j-1]]) {
					temp = array[j];
					array[j] = array[j - 1];
					array[j - 1] = temp;
				}
			}
		}
	}
	
	/**
	 * Predicted rating value in item by user who not yet rated to this item
	 * @param similarityMatrix 
	 * @param normalizedMatrix 
	 * @param arrUserRate
	 * @param item
	 * @param user
	 * @return
	 * @throws IOException 
	 */
	public static double predictedRatingByKNN(double[][] normalizedMatrix, double[][] similarityMatrix, int[] arrUserRateIndex,int item, int user) throws IOException{
		double numerator=0;
		double denominator=0;
		
		double[] arrUserRateSim = new double[arrUserRateIndex.length];
		double[] arrUserRateNor = new double[arrUserRateIndex.length];
		
		for (int i = 0; i < arrUserRateIndex.length; i++) {
			arrUserRateSim[i]=similarityMatrix[user][arrUserRateIndex[i]];
			arrUserRateNor[i]=normalizedMatrix[item][arrUserRateIndex[i]];
		}
		
		int i=0;
		while(i<KNEAREST) {
			try {
				numerator = numerator + (arrUserRateSim[i]*arrUserRateNor[i]);
				denominator= denominator +Math.abs(arrUserRateSim[i]);
			} catch (Exception e) {
				// TODO: handle exception
				break;
			}
			i++;
		}		
		return (double) (Math.round((numerator/denominator)*100.0)/100.0);
	}
	
	/**
	 * 
	 * @param fullMatrix
	 * @param normalizedMatrix
	 * @param user
	 */
	public static void reccommItem(double[][] fullMatrix, double[][] normalizedMatrix, int user) {
		
		int count=0;
		for (int i = 0; i < normalizedMatrix.length; i++) {
			if(normalizedMatrix[i][user]==0) { count++;  }
		}
		
		double[] arr = new double[count];
		
		int t=0;
		for (int i = 0; i < normalizedMatrix.length; i++) {
			if(normalizedMatrix[i][user]==0) 
			{ 
				arr[t]=fullMatrix[i][user]; 
				t++; 
			}
		}
		
		SortDecs(arr);
		
		int x=0;
		while(x<numItem && x<arr.length) {
			for (int i = 0; i < normalizedMatrix.length; i++) {
				if(normalizedMatrix[i][user]==0&&arr[x]==fullMatrix[i][user]) 
				{ 
					if(x>0) System.out.print("; ");
					System.out.print("Item:"+i);
					if(fullMatrix[i][user]>5)
						System.out.print("-Value:"+5);
					else System.out.print("-Value:"+fullMatrix[i][user]);
					x++; 
					if(x == numItem || x == arr.length) break;
				}
			}
		}
	}
	
	/**
	 * Show matrix on console
	 * @param matrix
	 */
	public static void dislayMatrix(double [][] matrix) {
		
		for (int i = 0;  i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j]+"\t");
			}
			System.out.println();
		} 
		System.out.println("------------");
	}
	
	public static void main(String args[])throws IOException{
		
		System.out.println("Recommendation System Ratings!!! LOADING.....");
		
		double[][] matrix = m100.CreateMatrix();
		double[][] normalizedMatrix = normalizedMatrix(matrix, arrAverageUser(matrix));
		double[][] similarityMatrix = similarityMatrix(normalizedMatrix);
		int[] arrUserRateIndex= null;
//		dislayMatrix(matrix);
//		for (double f : arrAverageUser(matrix)) {
//			System.out.print(f+"\t");
//		}
//		System.out.println();
//		System.out.println("--------------");
//		dislayMatrix(normalizedMatrix);
//		dislayMatrix(similarityMatrix);
//		double[][] fullMatrix = normalizedMatrix(matrix, arrAverageUser(matrix));
		//Predicted each item none rated by a user
//		for (int i = 0; i < normalizedMatrix.length; i++) {
//			for (int j = 0; j < normalizedMatrix[i].length; j++) {
//				if(normalizedMatrix[i][j]==0) {
//					try {
//						arrUserRateIndex =arrUserRateIndex(normalizedMatrix, similarityMatrix, i, j);
//						fullMatrix[i][j] = predictedRatingByKNN(normalizedMatrix, similarityMatrix, arrUserRateIndex, i, j); 
//					} catch (Exception e) {
//						//Cold-start problem
//						fullMatrix[i][j]=0;
//					}
//					
//				}
//			}
//			
//		}
//		dislayMatrix(fullMatrix);
//		convertMatrix(matrix ,fullMatrix, arrAverageUser(matrix));
//		dislayMatrix(fullMatrix);

//		for (int i = 0; i < fullMatrix[0].length; i++) {
//			System.out.print("Recommendation for USER ID ["+(i+1)+"]: ");
//			reccommItem(fullMatrix, normalizedMatrix, i);
//			System.out.println();
//		}
		
		int[] arrRating = m100.ArrayTest();
		double MSE=0;
		int dem=0;
		double[]arrPreRating =  new double [arrRating.length];
		for (int i = 0; i < normalizedMatrix[0].length; i++) {
			for (int j = 0; j < normalizedMatrix.length; j++) {
				if(normalizedMatrix[j][i]==0) {
					if(dem>=arrPreRating.length-1) break;
					try {
						arrUserRateIndex =arrUserRateIndex(normalizedMatrix, similarityMatrix, j, i);
						arrPreRating[dem] = predictedRatingByKNN(normalizedMatrix, similarityMatrix, arrUserRateIndex, j, i)+arrAverageUser(matrix)[j];
						MSE=Math.pow((arrRating[dem]-arrPreRating[dem]), 2)+MSE;
						dem++;
						System.out.println(MSE);
					} catch (Exception e) {
						//Cold-start problem
						arrPreRating[j]=0;
					}
					
				}
			}
			
		}
		MSE=(double)1/dem*(MSE);
		System.out.println("MSE:"+MSE); 
		System.out.println((double)Math.sqrt(MSE));
		System.out.println("End System");
	}
}
