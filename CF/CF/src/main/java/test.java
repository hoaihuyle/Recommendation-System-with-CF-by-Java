import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import untils.MyUntils;

public class test {

	private static final int numItem = 3;
	private static final int KNEAREST = 10;
	private static final String fileName = MyUntils.FileLocation();
	private static final int ITEMS = 10;//10 1682
	private static final int USERS = 7;//7 943

	/**
	 * Initialize the matrix
	 * Read data from movielens with data unknown set -1 
	 * @return matrix 2d
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int[][] CreateMatrix()throws FileNotFoundException, IOException
	{
		// Initialize the matrix with -1 for all elements
		int[][] matrix = new int[ITEMS][USERS];
//		int[][] matrix = new int[5][7];//[item][user]
		for (int i = 0; i<matrix.length; ++i)
		{
			for (int j = 0; j<matrix[0].length; ++j)
			{
				matrix[i][j] = -1;
			}
		}
		
		try {
			// Read the input values and form the full matrix 
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringTokenizer st = null;
			String row;
			while ((row = br.readLine()) != null)
			{
				st = new StringTokenizer(row, ",");
		        
				while(st.hasMoreTokens())
	            {
					int user = Integer.parseInt(st.nextToken());
					int movie = Integer.parseInt(st.nextToken());
					int rating = Integer.parseInt(st.nextToken());
					matrix[movie-1][user-1] = rating; 
				}		
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println("File Read Error");
		}
		
		return matrix;
	}

	/**
	 * Average User
	 * @param matrix Average Column
	 * @return array with length = matrix[0].length
	 */
	public static int[] arrAverageUser(int [][] matrix) {
		
		int arr[] = new int[matrix[0].length]; 
		int j=0;
		while (j<matrix[0].length) {
			int sum=0;
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
				arr[j]=(int) (Math.round((sum/count)));
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
	public static int[][] normalizedMatrix(int [][] matrix, int [] arrAverageUser){
		
		int[][] normatrix = new int[matrix.length][matrix[0].length];//[item][user]
		
		for (int i = 0; i < normatrix[0].length; i++) {
			for (int j = 0; j < normatrix.length; j++) {
				if(matrix[j][i]!=-1)
				normatrix[j][i]= matrix[j][i] - arrAverageUser[i];
				else normatrix[j][i] = 0;
			}
		}
//		dislayMatrix(normatrix);
		return normatrix;
	}
	
	/**
	 * Convert normalizedMatrix to originalMatrix with full element was predicted
	 * @param matrix
	 * @param fullMatrix 
	 * @param arr
	 * @return matrix 2d
	 */
	public static void convertMatrix(int [][] matrix, int[][] fullMatrix, int [] arrAverageUser){
				
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
	public static int[][] similarityMatrix(int [][] normalizedMatrix){
		
		int[][] similarityMatrix = new int[normalizedMatrix[0].length][normalizedMatrix[0].length];//[item][user]
		
		//Initialize the matrix similarity between user to user
		for (int i = 0; i < similarityMatrix[0].length; i++) {
			for (int j = 0; j < similarityMatrix[0].length; j++) {
				//Initialize the vector have length is the total rows in matrix normalizedMatrix
				int [] arrVec1 = new int[ normalizedMatrix.length];
				int [] arrVec2 = new int[ normalizedMatrix.length];
				
				for (int j2 = 0; j2 < normalizedMatrix.length; j2++) {
					arrVec1[j2] =  normalizedMatrix[j2][i];
					arrVec2[j2] =  normalizedMatrix[j2][j];
				}
				
				//compute similarity by cosine function
				int multipVec =0;
				int sizeVec1 =0;
				int sizeVec2 =0;

				for (int l = 0; l < normalizedMatrix.length; l++) {										
					multipVec= arrVec1[l]*arrVec2[l]+multipVec;
					sizeVec1= (int) (Math.pow(arrVec1[l], 2) + sizeVec1);
					sizeVec2= (int) (Math.pow(arrVec2[l], 2) + sizeVec2);
				}
				similarityMatrix[i][j] = (int) (Math.round(multipVec/(Math.sqrt(sizeVec1)*Math.sqrt(sizeVec2))));
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
	public static int[] arrUserRateIndex(int [][] normalizedMatrix, int [][] similarityMatrix, int item, int user){
		
		int count=0;
		
		for (int f : normalizedMatrix[item]) {
			if(f!=0) { count++;  }
		}
		
		int [] arr = new int[count];
		//users who rated item
		int i=0;
		int j=0;
		
		for (int f : normalizedMatrix[item]) {
			if(f!=0) {arr[j]=i; j++;}
			i++;
		}
		SortDecsIndex(arr, similarityMatrix, user);
		return arr;
	}
		
	/**
	 * Sort Array DECS
	 * @param array
	 */
	public static void SortDecs(int[] array) {
		int temp =array[0];
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
	public static void SortDecsIndex(int[] array, int[][]similarityMatrix, int user) {
		int temp =similarityMatrix[user][array[0]];
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
	public static int predictedRatingByKNN(int[][] normalizedMatrix, int[][] similarityMatrix, int[] arrUserRateIndex,int item, int user) throws IOException{
		int numerator=0;
		int denominator=0;
		
		int[] arrUserRateSim = new int[arrUserRateIndex.length];
		int[] arrUserRateNor = new int[arrUserRateIndex.length];
		
		for (int i = 0; i < arrUserRateIndex.length; i++) {
			arrUserRateSim[i]=similarityMatrix[user][arrUserRateIndex[i]];
			arrUserRateNor[i]=normalizedMatrix[item][arrUserRateIndex[i]];
		}
		
		int i=0;
		while(i<KNEAREST) {
			try {
//				System.out.println("SIM:"+arrUserRateSim[i]+" NOR:"+arrUserRateNor[i]+" i:"+i);
				numerator = numerator + (arrUserRateSim[i]*arrUserRateNor[i]);
				denominator= denominator +Math.abs(arrUserRateSim[i]);
//				System.out.println("denominator:"+denominator+" numerator:"+numerator+" i:"+i);
			} catch (Exception e) {
				// TODO: handle exception
				break;
			}
			i++;
		}		
		return (int) (Math.round((numerator/denominator)));
	}
	
	/**
	 * 
	 * @param fullMatrix
	 * @param normalizedMatrix
	 * @param user
	 */
	public static void reccommItem(int[][] fullMatrix, int[][] normalizedMatrix, int user) {
		
		int count=0;
		for (int i = 0; i < normalizedMatrix.length; i++) {
			if(normalizedMatrix[i][user]==0) { count++;  }
		}
		
		int[] arr = new int[count];
		
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
					System.out.print("-Value:"+fullMatrix[i][user]);
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
	public static void dislayMatrix(int [][] matrix) {
		
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
		
		System.out.println((double)Math.sqrt(2.1018952847642374));
		
		System.out.println("End System");
	}

}
