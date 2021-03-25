import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.StringTokenizer;

import untils.MyUntils;

public class ReadMatrix {
	
	static LinkedList lk = new LinkedList(); 
	/**
	 * Initialize the matrix
	 * Read data from movielens with data unknown set -1 
	 * @return matrix 2d
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public double[][] CreateMatrix()throws FileNotFoundException, IOException
	{
		final int ITEMS = 1682;//10 1682-1664 3952
		final int USERS = 943;//7 943-943 6040
		String fileName = MyUntils.FileLocation();
		// Initialize the matrix with -1 for all elements
		double[][] matrix = new double[ITEMS][USERS];
		
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
	 * Array test
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int[] ArrayTest()throws FileNotFoundException, IOException
	{
		String fileName = MyUntils.FileLocationTest();
		int[] arrRating = null;
		// Creating object of class linked list 
		LinkedList<Integer> linkedList = new LinkedList<>();
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
					st.nextToken();
					st.nextToken();
					int rating = Integer.parseInt(st.nextToken());
					linkedList.add(rating);
				}	
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println("File Read Error");
		}
		  Object[] objectAarray = linkedList.toArray();
	      int length = objectAarray.length;
	      arrRating = new int[length];
	      for(int i =0; i < length; i++) {
	    	  arrRating[i] = (int) objectAarray[i];
	      }
//	      System.out.println("Contents of the array: \n"+Arrays.toString(arrRating));
		return arrRating;
	}
	
	public int[][] CreateMatrixInt1M()throws FileNotFoundException, IOException
	{
		final int ITEMS = 3952;//10 1682 3952
		final int USERS = 6040;//7 943 6040
		String fileName = MyUntils.FileLocation1M();
		// Initialize the matrix with -1 for all elements
		int[][] matrix = new int[6040][3952];
		
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
					matrix[user-1][movie-1] = rating; 
				}		
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println("File Read Error");
		}
		
		return matrix;
	}



}
