package org.taalmaan.detect.coOccur;

import java.util.ArrayList;

public class TaalEnvelope {

	private ArrayList<Double> envelopeXData;
	private ArrayList<Double> envelopeYData;
	private double filterCoeff;
	private int dsRate;

	
	public ArrayList<Double> getEnvelopeXData() {
		return envelopeXData;
	}
	public void setEnvelopeXData(ArrayList<Double> envelopeXData) {
		this.envelopeXData = envelopeXData;
	}
	public ArrayList<Double> getEnvelopeYData() {
		return envelopeYData;
	}
	public void setEnvelopeYData(ArrayList<Double> envelopeYData) {
		this.envelopeYData = envelopeYData;
	}
	
	
	public TaalEnvelope (Double samplingRate, ArrayList<Double> inputYData, ArrayList<Double> inputXData){
		setDefaulParameters(samplingRate);
		calculateEnvelope(samplingRate, inputYData, inputXData);			
	}
	public void calculateEnvelope(Double samplingRate, ArrayList<Double> inputYData, ArrayList<Double> inputXData) {
		envelopeXData = new ArrayList<Double>();
		envelopeYData = new ArrayList<Double>();
		
		ArrayList<Double> frstLevelData = new ArrayList<Double>();
		ArrayList<Double> secondLevelData = new ArrayList<Double>();
		ArrayList<Double> tempXData = new ArrayList<Double>();
		ArrayList<Double> tempYData = new ArrayList<Double>();
		double[] aFilter = new double[2];
		double bFilter = 0;
		double valueToAdd = 0;
		
		aFilter[0] = 0;
		aFilter[1] = -1*filterCoeff;
		bFilter = 1-filterCoeff;
		
		for (int i=inputYData.size()-1; i>=0; i--) {
			frstLevelData.add(Math.abs(inputYData.get(i)));
		}
		
		secondLevelData.add(bFilter*frstLevelData.get(0));
		valueToAdd = bFilter*frstLevelData.get(1) - aFilter[1]*secondLevelData.get(0);
		secondLevelData.add(valueToAdd);
		for (int i=2; i<inputYData.size(); i++) {
			valueToAdd = bFilter*frstLevelData.get(i) - aFilter[1]*secondLevelData.get(i-1);
			secondLevelData.add(valueToAdd);
		}
		int ii = 0;
		for (int i=inputYData.size()-1; i>=0; i--) {
			frstLevelData.set(ii,Math.abs(secondLevelData.get(i)));
			ii = ii+1;
		}
		
		secondLevelData.set(0,bFilter*frstLevelData.get(0));
		valueToAdd = bFilter*frstLevelData.get(1) - aFilter[1]*secondLevelData.get(0);
		secondLevelData.set(1,valueToAdd);
		for (int i=2; i<inputYData.size(); i++) {
			valueToAdd = bFilter*frstLevelData.get(i) - aFilter[1]*secondLevelData.get(i-1);
			secondLevelData.set(i,valueToAdd);
		}
		int i = 0;
		double timeToAdd = 0;
		while (i<inputYData.size()){
			timeToAdd = inputXData.get(i);
			tempXData.add(timeToAdd);
			tempYData.add(secondLevelData.get(i));
			i = i+dsRate;
		}
		
		for (i=0; i<(tempXData.size()-1); i++) {
			valueToAdd = tempYData.get(i+1)-tempYData.get(i);
				valueToAdd = ((valueToAdd + Math.abs(valueToAdd))/2)*(samplingRate/10);
			envelopeYData.add(valueToAdd);
			envelopeXData.add(tempXData.get(i));
		}
		//this.envelopeYData = envelopeYData;
		//this.envelopeXData = envelopeXData;
					
	}
	private void setDefaulParameters (Double samplingRate) {
		this.filterCoeff = Math.exp(-1/(0.02*samplingRate));
		this.dsRate = 16;
	}
}
