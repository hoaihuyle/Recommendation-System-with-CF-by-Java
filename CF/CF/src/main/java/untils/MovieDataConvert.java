package untils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MovieDataConvert {

	/**
	 * cat u.data | cut -f1,2,3 | tr "\\t" ","
	 * @throws IOException 
	 * 
	 */
	
	public static void main(String[] args) throws IOException {
		
//		BufferedReader br = new BufferedReader(new FileReader("E:\\documents\\Documents\\KLTN2019\\db\\ml-100k\\ua.base"));
		BufferedReader br = new BufferedReader(new FileReader("E:\\documents\\Documents\\KLTN2019\\db\\ml-100k\\ub.base"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\documents\\Documents\\KLTN2019\\db\\ml-100kConvert\\ub-base.txt"));
		
		String line;
		while((line = br.readLine()) != null) {
			String[] values = line.split("\\t", -1);
			bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
		}
		
		br.close();
		bw.close();

	}

}
