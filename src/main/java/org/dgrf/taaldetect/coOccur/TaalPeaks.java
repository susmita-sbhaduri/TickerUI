package org.dgrf.taaldetect.coOccur;

import java.util.ArrayList;
import java.util.Collections;

public class TaalPeaks {

	private ArrayList<Double> peakXData;
	private ArrayList<Double> peakYData;
	
	TaalPeaks (ArrayList<Double> inputYData, ArrayList<Double> inputXData, Double cThreshold){
		calculatePeaks(inputYData, inputXData, cThreshold);	

	}
	public ArrayList<Double> getPeakXData() {
		return this.peakXData;
	}
	public void setPeakXData(ArrayList<Double> peakXData) {
		this.peakXData = peakXData;
	}
	public ArrayList<Double> getPeakYData() {
		return this.peakYData;
	}
	public void setPeakYData(ArrayList<Double> peakYData) {
		this.peakYData = peakYData;
	}
	
	public void calculatePeaks(ArrayList<Double> inputYData, ArrayList<Double> inputXData, Double cThreshold) {
		peakXData = new ArrayList<Double>();
		peakYData = new ArrayList<Double>();
		
		ArrayList<Double> copyXData = new ArrayList<Double>();
		ArrayList<Double> copyYData = new ArrayList<Double>();
		ArrayList<Double> normYData = new ArrayList<Double>();
		ArrayList<Integer> mx = new ArrayList<Integer>();
		ArrayList<Integer> finalMx = new ArrayList<Integer>();
		double valueToAdd = 0;
		double valueToChkLeft = 0;
		double valueToChkRight = 0;
		double maxToNorm = 0;
		double bufmin = 0;
		double bufmax = 0;
		double oldbufmin = 0;
		double temp = 0;
		int currPeak = 0;
		int j = 0;
		int from = 0;
		int to = 0;
		
		copyXData = inputXData;
		copyYData = inputYData;
		
		maxToNorm = (Collections.max(inputYData)-Collections.min(inputYData));
		normYData.add(Double.NEGATIVE_INFINITY);
		for (int i=0; i<inputXData.size(); i++) {
			valueToAdd = inputYData.get(i)/maxToNorm;
			normYData.add(valueToAdd);
		}
		normYData.add(Double.NEGATIVE_INFINITY);
//		mx.add(normYData.get(0));
		for (int i=1; i<(normYData.size()-1); i++) {
			valueToChkLeft = normYData.get(i) - normYData.get(i-1);
			valueToChkRight = normYData.get(i) - normYData.get(i+1);
			if ((valueToChkLeft > 0 && valueToChkRight >0) && (normYData.get(i) >= cThreshold)) {
//				mx.add(normYData.get(i));
				mx.add(i);
			}
		}
//		TaalFileHandler.writeIntToFile("/Users/dgrfIII/Documents/test/peakp.txt", mx);
		currPeak = mx.get(0);
		bufmin=Double.POSITIVE_INFINITY;
		bufmax = normYData.get(currPeak);
		from = 0;
		to = currPeak;
		if(to-from>0){
			ArrayList<Double> sList = new ArrayList<Double>(normYData.subList(from, to));
			oldbufmin = Collections.min(sList);
		}
		else{
			oldbufmin = normYData.get(from);
		}
		
		for (int i=1; i<mx.size(); i++) {
			from = mx.get(i-1)+1;
			to = mx.get(i);
			if(to-from>0){
				ArrayList<Double> sList = new ArrayList<Double>(normYData.subList(from, to));
				temp = Collections.min(sList);
			}
			else{
				temp = normYData.get(from);
			}
			if(bufmin > temp)
				bufmin = temp;
			if((bufmax - bufmin) < cThreshold) {
				if (normYData.get(mx.get(i)) > bufmax){
					j = i;
					currPeak = mx.get(i);
					bufmax = normYData.get(currPeak);
					if(oldbufmin>bufmin){
						oldbufmin = bufmin;
					}
					bufmin=Double.POSITIVE_INFINITY;
				}
				else if((normYData.get(mx.get(i))-bufmax)<=0){
					if(oldbufmin>bufmin){
						oldbufmin = bufmin;
					}
					if(bufmax<normYData.get(mx.get(i))){
						bufmax = normYData.get(mx.get(i));
					}
				}
			}
			else {
				if((bufmax - oldbufmin) < cThreshold) {
					if(oldbufmin>bufmin){
						oldbufmin = bufmin;
					}
				}
				else{
					finalMx.add(currPeak);
					oldbufmin = bufmin;
				}
				bufmax = normYData.get(mx.get(i));
				j = i;
				currPeak = mx.get(i);
				bufmin=Double.POSITIVE_INFINITY;
			}
		}
//		TaalFileHandler.writeIntToFile("/Users/dgrfIII/Documents/test/peak.txt", finalMx);
		from = mx.get(j)+1;
		to = normYData.size();
		if(to-from>0){
			ArrayList<Double> sList = new ArrayList<Double>(normYData.subList(from, to));
			temp = Collections.min(sList);
		}
		else{
			temp = normYData.get(from);
		}
		if((bufmax - oldbufmin >= cThreshold)&&((bufmax - temp) >= cThreshold)){
			finalMx.add(mx.get(j));
		}
		for (int i=0; i<finalMx.size(); i++) { 
			temp = copyXData.get(finalMx.get(i)-1);
			peakXData.add(temp);
			temp = copyYData.get(finalMx.get(i)-1);
			peakYData.add(temp);
		}

	}
}
