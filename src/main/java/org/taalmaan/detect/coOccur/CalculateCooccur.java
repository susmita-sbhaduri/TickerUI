package org.taalmaan.detect.coOccur;

import java.util.ArrayList;
import java.util.Arrays;

import org.taalmaan.detect.taalUtill.TaalDSP;
import org.taalmaan.detect.taalUtill.TaalFileHandler;

public class CalculateCooccur {

	private int[][] coOccurMatrix;
    private double[][] percussiveDurList;
    private int[][] percussiveCountList;
	private ArrayList<Double> songTextArr;
	private int lengthArray;
	
	private double lowestFreq;
	private double samplingRate;
	private int noBand;
	private int reqBand;
	private int nBits;
	private double cThr;
	private double iniTolerance;

	public int[][] getCoOccurMatrix() {
		return coOccurMatrix;
	}
	public void setCoOccurMatrix(int[][] coOccurMatrix) {
		this.coOccurMatrix = coOccurMatrix;
	}
	
	
	public CalculateCooccur (ArrayList<Double> songTextArr) {
		this.songTextArr = songTextArr;
		setDefaulParameters();
		cooccrMatrix(this.songTextArr);			
	}
	public void cooccrMatrix(ArrayList<Double> songTextArr) {
		coOccurMatrix = new int[16][16];
					
		ArrayList<Double> bayanRawData = new ArrayList<Double>();
		ArrayList<Double> bayanXData = new ArrayList<Double>();
		ArrayList<Double> bayanYData = new ArrayList<Double>();
		ArrayList<Double> percusXData = new ArrayList<Double>();
		ArrayList<Double> percusYData = new ArrayList<Double>();
		ArrayList<Double> bayanDuration = new ArrayList<Double>();
		ArrayList<Integer> percusCount = new ArrayList<Integer>();
		
		ArrayList<Double> normData = new ArrayList<Double>();			
		ArrayList<Double> normPerData = new ArrayList<Double>();
		ArrayList<Double> tempData = new ArrayList<Double>();
		ArrayList<Double> timeData = new ArrayList<Double>();			
		ArrayList<Double> envXData = new ArrayList<Double>();
		ArrayList<Double> envYData = new ArrayList<Double>();
		ArrayList<Double> peakXData = new ArrayList<Double>();
		ArrayList<Double> peakYData = new ArrayList<Double>();
		ArrayList<Double> peakBigXData = new ArrayList<Double>();
		ArrayList<Double> peakBigYData = new ArrayList<Double>();
		double x1 = 0;
		double x2 = 0;
		double y1 = 0;
		double y2 = 0;
		
		double valueToCheck = 0;
		int indeX=0;
		//enforcing 60 secs duration
		double timeIncrement = Math.floor(songTextArr.size()/samplingRate)/(songTextArr.size()-1);
		
		normPerData = TaalFileHandler.normaliseDataArray(songTextArr);
		bayanRawData = TaalDSP.filterBand(samplingRate, normPerData, lowestFreq, noBand, reqBand);
		normData = TaalFileHandler.normaliseDataArray(bayanRawData);
		
		
		/*normalise data with respect to the wavread of matlab */  
		for (int i=0; i<normData.size(); i++) {
		//	valueToCheck = normData.get(i);
		//	if((valueToCheck == Math.floor(valueToCheck)) && !Double.isInfinite(valueToCheck)){
			//if(valueToCheck == Math.floor(valueToCheck)){
		//		tempData.add(valueToCheck);
		//	}else{
		//		tempData.add((double) Math.round(Math.pow(2,(nBits-1))*valueToCheck));
		//	}
			valueToCheck = Math.round(Math.pow(2,(nBits-1))*normData.get(i));
			if (valueToCheck < (-1*Math.pow(2,(nBits-1)))){
				valueToCheck = -1*Math.pow(2,(nBits-1));
			}
			if (valueToCheck > (Math.pow(2,(nBits-1))-1)){
				valueToCheck = Math.pow(2,(nBits-1))-1;
			}
			valueToCheck = valueToCheck/Math.pow(2,(nBits-1));
			tempData.add((double) valueToCheck);
			
			timeData.add((double) i*timeIncrement);
		}
		bayanRawData = tempData;
		TaalEnvelope bayanEnv = new TaalEnvelope(samplingRate, bayanRawData, timeData);
		envXData = bayanEnv.getEnvelopeXData();
		envYData = bayanEnv.getEnvelopeYData();
		TaalPeaks bayanPeaks = new TaalPeaks(envYData, envXData, cThr);
		peakXData = bayanPeaks.getPeakXData();
		peakYData = bayanPeaks.getPeakYData();
		
	//	Collections.sort(peakYData);
		for (int i=0; i<peakYData.size(); i++) {
			if(peakYData.get(i)>(TaalFileHandler.findMeanValue(peakYData))+
					TaalFileHandler.findSdValue(peakYData)){
				peakBigYData.add(peakYData.get(i));
			}
		}
		for (int i=0; i<peakBigYData.size(); i++) {
			valueToCheck = peakBigYData.get(i);
			indeX = peakYData.indexOf(valueToCheck);
			peakBigXData.add(peakXData.get(indeX));
		}
		
		x1 = 0;// ignoring the first and generally noisy peak
		y1 = 0;
		
		for (int i=0; i<peakBigXData.size(); i++) {
			x2 = peakBigXData.get(i);
			y2 = peakBigYData.get(i);
			if(x2-x1 <= iniTolerance){
				if(y1 < y2){
					 y1 = y2; 
			         x1 = x2;
				}
			}
			else{
				bayanXData.add(x1);
				bayanYData.add(y1);
				y1 = y2;
		        x1 = x2;
			}
		}
		
		if(y1>0){
			bayanXData.add(x1);
			bayanYData.add(y1);
		}
		
		TaalEnvelope percEnv = new TaalEnvelope(samplingRate, normPerData, timeData);
		envXData = percEnv.getEnvelopeXData();
		envYData = percEnv.getEnvelopeYData();
		TaalPeaks percPeaks = new TaalPeaks(envYData, envXData, cThr);
		peakXData = percPeaks.getPeakXData();
		peakYData = percPeaks.getPeakYData();
		
		x1 = peakXData.get(0);// ignoring the first and generally noisy peak
		y1 = peakYData.get(0);
		
		for (int i=1; i<peakXData.size(); i++) {
			x2 = peakXData.get(i);
			y2 = peakYData.get(i);
			if(x2-x1 <= iniTolerance){
				if(y1 < y2){
					 y1 = y2; 
			         x1 = x2;
				}
			}
			else{
				percusXData.add(x1);
				percusYData.add(y1);
				y1 = y2;
		        x1 = x2;
			}
		}
		
		if(y1>0){
			percusXData.add(x1);
			percusYData.add(y1);
		}
		
		int j = 1;
		int count_per = 0;
		for (int i=0; i<(bayanXData.size()-1); i++) {
			count_per = 0;
			while(true){
				if((percusXData.get(j) > bayanXData.get(i)) && (percusXData.get(j) < bayanXData.get(i+1))){
					count_per = count_per+1;
					j = j+1;
					if(j >= percusXData.size()){
						break;
					}		                
				}
				else{
					break;
				}
			}
			valueToCheck = bayanXData.get(i+1)-bayanXData.get(i);
			bayanDuration.add(valueToCheck); // duration of time between consecutive bayan strokes.
			percusCount.add(count_per); //duration of per stroke for each i matra
		}
		lengthArray = percusCount.size()-1;
		percussiveCountList = new int[lengthArray][2];
		percussiveDurList = new double[lengthArray][2];
		Integer[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
				
		for (int i=0; i<(percusCount.size()-1); i++) {
			if ( Arrays.asList(a).contains(percusCount.get(i)) && Arrays.asList(a).contains(percusCount.get(i+1)) )  {
				coOccurMatrix[percusCount.get(i)-1][percusCount.get(i+1)-1] =
						coOccurMatrix[percusCount.get(i)-1][percusCount.get(i+1)-1]+1;
				percussiveCountList[i][0]=percusCount.get(i);
				percussiveCountList[i][1]=percusCount.get(i+1);
				percussiveDurList[i][0]=bayanDuration.get(i)/percusCount.get(i);
				percussiveDurList[i][1]=bayanDuration.get(i+1)/percusCount.get(i+1);
			}
		}
		

				
		
	}
	public int getLengthArray() {
		return lengthArray;
	}
	public void setLengthArray(int lengthArray) {
		this.lengthArray = lengthArray;
	}
	public double[][] getPercussiveDurList() {
		return percussiveDurList;
	}
	public void setPercussiveDurList(double[][] percussiveDurList) {
		this.percussiveDurList = percussiveDurList;
	}
	public int[][] getPercussiveCountList() {
		return percussiveCountList;
	}
	public void setPercussiveCountList(int[][] percussiveCountList) {
		this.percussiveCountList = percussiveCountList;
	}
	private void setDefaulParameters () {
		this.lowestFreq = 50.0;
		this.samplingRate = 44100.0;
		this.noBand = 20;
		this.reqBand = 2;
		this.nBits = 16;
		this.cThr = 0.01;
		this.iniTolerance = 0.1;
		
		//LogUtil.setLogBase(2);
	}
}
