package org.dgrf.taaldetect.taalUtill;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaalFileHandler {

	public static ArrayList<Double> readRawDataFile (String fname) {
		ArrayList<Double> songRawData = new ArrayList<Double>();
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fname)));
			
			String RawData = br.readLine();
			Double RawDataInFloat;
			while (RawData != null) {
				
				RawDataInFloat = Double.parseDouble(RawData);
				RawData = br.readLine();
				songRawData.add(RawDataInFloat);
			}
			br.close();
		}
		catch (FileNotFoundException fe) {
			Logger.getLogger(TaalFileHandler.class.getName()).log(Level.SEVERE,"File Not found" );
                        
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return songRawData;
	}
	
	public static ArrayList<InputDetails> readParameterFile (String fname) {
		ArrayList<InputDetails> inpParm = new ArrayList<InputDetails>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fname)));
			
			String RawData = br.readLine();
			
			while (RawData != null) {
				InputDetails temp = new InputDetails();
				//http://stackoverflow.com/questions/19843506/why-does-my-arraylist-contain-n-copies-of-the-last-item-added-to-the-list
				String[] values = RawData.split(",");
				temp.setInputTala(values[0]); 
				temp.setInputSongName(values[1]);
				temp.setInputTempo(Double.parseDouble(values[2]));
				temp.setFileCount(Integer.parseInt(values[3]));
				inpParm.add(temp);
				RawData = br.readLine();
			}
			br.close();
			
		}
		catch (FileNotFoundException fe) {
			Logger.getLogger(TaalFileHandler.class.getName()).log(Level.SEVERE,"File Not found" );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return inpParm;
	}
	public static void writeTaalDataFile (String fname,ArrayList<TalaDetails> taalList) {
		try {
			
			File file = new File(fname);
			 
			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter fw =  new OutputStreamWriter(
					new FileOutputStream(file.getAbsoluteFile()), "UTF8");
			
			BufferedWriter bw = new BufferedWriter(fw);
			TalaDetails TaalDet = new TalaDetails();
			bw.write("Tala,Song-name,C-tala,Actual_tempo,Tempo\n");
			for (int i=0;i<taalList.size();i++) {				
				TaalDet = taalList.get(i);
				bw.write(TaalDet.getInputTala()+","+ TaalDet.getInputSongName()+","
						+ TaalDet.getOutputTala()+","+TaalDet.getInputTempo()+","+TaalDet.getOutputTempo()+"\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[][] readCsvDataFile (String fname) {
		int[][] countArray = new int[100][2];
		int[][] countArrayRet = null;
		int i = 0;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fname)));
			
			String RawData = br.readLine();
			while (RawData != null) {
				String[] values = RawData.split(",");
				countArray[i][0] = Integer.parseInt(values[0]);
				countArray[i][1] = Integer.parseInt(values[1]);
				i=i+1;
				RawData = br.readLine();
			}
			br.close();
			countArrayRet = new int[i][2];
			for (int j=0; j<i; j++) {
				countArrayRet[j][0] = countArray[j][0];
				countArrayRet[j][1] = countArray[j][1];
			}
		}
		catch (FileNotFoundException fe) {
			Logger.getLogger(TaalFileHandler.class.getName()).log(Level.SEVERE,"File Not found" );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return countArrayRet;
	}

	public static ArrayList<Double> normaliseDataArray(ArrayList<Double> rawData) {
		int i;
		Double valueToAdd= (double) 0;
		ArrayList<Double> normalisedData = new ArrayList<Double>();
		try{
		
			Double maxDataValue = findMaxValue(rawData);
		
			for (i=0;i<rawData.size();i++) {
				valueToAdd = rawData.get(i)/maxDataValue;
				normalisedData.add(valueToAdd);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return normalisedData;
	}
	
	public static Double findMaxValue(ArrayList<Double> rawData) {
		int i;
		if (rawData.size() == 0) {
			return null;
		}
		Double maxVal = Math.abs(rawData.get(0));
		for (i=0;i<rawData.size();i++) {
			if (maxVal<Math.abs(rawData.get(i))) {
				maxVal = Math.abs(rawData.get(i));
			}
		}
/*		if (maxVal<=0) {
			return (double) 0;
		} else {
			return maxVal;
		}*/
		
		return maxVal;
		
	}
	
	
	public static int findMaxValMatrix(int[][] rawMatrix, int row, int col) {
		int maxVal = Math.abs(rawMatrix[0][0]);
		if (rawMatrix.length == 0) {
			return 0;
		}
		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				if (maxVal<Math.abs(rawMatrix[i][j])) {
					maxVal = Math.abs(rawMatrix[i][j]);
				}
			}
		}
		
		return maxVal;
		
	}
	
	public static int[][] findMaxValIndex(int[][] rawMatrix, int row, int col, int maxVal) {
		int[][] maxRowCol = new int[row*col][2];
		int k =0;
		if (rawMatrix.length == 0) {
			return rawMatrix;
		}
		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				if (rawMatrix[i][j] == maxVal) {
					maxRowCol[k][0] = i;
					maxRowCol[k][1] = j;
					k = k+1;
				}
			}
		}
		
		int[][] maxRowColRet = new int[k][2];
		for (int i=0; i<k; i++) {
			maxRowColRet[i][0] = maxRowCol[i][0];
			maxRowColRet[i][1] = maxRowCol[i][1];
		}
		return maxRowColRet;
		
	}
	public static Double findMeanValue(ArrayList<Double> rawData) {
		double total = 0;

        for ( int i= 0;i < rawData.size(); i++)
        {
            double currentNum = rawData.get(i);
            total+= currentNum;
        }
        return total/rawData.size();
		
	}
	
	public static Double findSdValue(ArrayList<Double> rawData) {
	//	double sum = getSum();
        //STEP 1
        double avg = findMeanValue(rawData);
        double calc1 = 0;
        double calc2 = 0;
        double count = rawData.size();
        double stdDeviation = 0;
 
        
 
        /*STEP 2:  Calculate the subtraction of Avg/Mean from each value
        Create an iterator in order to access(get) the values in inputList
         */
        for (int i = 0; i < count; i++) {
            calc1 = rawData.get(i) - avg;
 
        // STEP 3:  Square root each remainder from STEP 2
 
            calc1 = Math.pow(calc1, 2);
 
        // STEP 4:  Total the all remainders from STEP 3
            calc2 = calc2 + calc1;
            // Add calc2 into a new array for further calculations
        }
 
        // STEP 5:  Divide total from STEP 4 by one less than the count of all numbers
            calc2 = calc2 / (count-1);
 
        // STEP 6:  Square root the value from STEP 5 = Standard Deviation
            stdDeviation = Math.sqrt(calc2);
 
        // Return Standard Deviation Value
        return stdDeviation;
		
	}
	public static void writeBandToFile (String fname,ArrayList<Double> InputBandData) {
		try {
				
			File file = new File(fname);
				 
			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter fw =  new OutputStreamWriter(
				new FileOutputStream(file.getAbsoluteFile()), "UTF8");
				
			BufferedWriter bw = new BufferedWriter(fw);
	
				
			for (int i=0;i<InputBandData.size();i++) {
				bw.write(InputBandData.get(i)+"\n");	
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	public static void writeIntToFile (String fname,ArrayList<Integer> InputBandData) {
		try {
				
			File file = new File(fname);
				 
			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter fw =  new OutputStreamWriter(
				new FileOutputStream(file.getAbsoluteFile()), "UTF8");
				
			BufferedWriter bw = new BufferedWriter(fw);
	
				
			for (int i=0;i<InputBandData.size();i++) {
				bw.write(InputBandData.get(i)+"\n");	
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
	}
}
