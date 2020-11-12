package org.dgrf.taaldetect.taalUtill;

import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

public class TaalDSP {

	/**
	 * Reads a file with one double value per line into and Arraylist for PSVG analysis.
	 * @param fname File Name containing the data for the time series.
	 * @return Arraylist containing values for the time series.
	 */
		public static ArrayList<Double> filterBand (Double samplingRate, ArrayList<Double> tablaRawData, Double lowestFreq, int noBand, int reqBand) {
			ArrayList<Double> bayanRawData = new ArrayList<Double>();
			double[] xTemp = new double[tablaRawData.size()];
			//double[] yTemp = new double[tablaRawData.size()];
			double[][] Hdk = new double[4][6];
			double[] a = new double[3];
			double[] b = new double[3];
			
			try{
				
				
				for (int i = 0; i < tablaRawData.size(); i++) {
					bayanRawData.add(tablaRawData.get(i));
			         
			    }
				double[][][] multi = ERBFilters (samplingRate,noBand,lowestFreq);
			    Hdk = multi[reqBand-1];
			    for (int i=0; i<4; i++) {
				    a[0] = Hdk[i][3];
				    a[1] = Hdk[i][4];
				    a[2] = Hdk[i][5];
				    
				    b[0] = Hdk[i][0];
				    b[1] = Hdk[i][1];
				    b[2] = Hdk[i][2];
				    
				    for (int ii = 0; ii < tablaRawData.size(); ii++) {
				         xTemp[ii]=0.0;
				         
				    }
				    xTemp[0] = b[0]*bayanRawData.get(0);
				    xTemp[1] = b[0]*bayanRawData.get(1)+b[1]*bayanRawData.get(0) - a[1]*xTemp[0];
				    xTemp[2] = b[0]*bayanRawData.get(2)+b[1]*bayanRawData.get(1)+b[2]*bayanRawData.get(0) - a[1]*xTemp[1]
				    		- a[2]*xTemp[0];
				    
				    for (int iii = 3; iii < bayanRawData.size(); iii++) {			    	
				    	xTemp[iii]=b[0]*bayanRawData.get(iii)+b[1]*bayanRawData.get(iii-1)+b[2]*bayanRawData.get(iii-2)
				    			-a[1]*xTemp[iii-1]-a[2]*xTemp[iii-2];
				    			
				    }
				    for (int ii = 0; ii < tablaRawData.size(); ii++) {
				    	bayanRawData.set(ii,xTemp[ii]);
				    }
				}
			   
			   
			}
			
		/*	try{
				bayanRawData = tablaRawData;
				
			    double[][][] multi = ERBFilters (samplingRate,noBand,lowestFreq);
			    Hdk = multi[reqBand-1];
			    for (int i=0; i<4; i++) {
				    a[0] = Hdk[i][3];
				    a[1] = Hdk[i][4];
				    a[2] = Hdk[i][5];
				    
				    b[0] = Hdk[i][0];
				    b[1] = Hdk[i][1];
				    b[2] = Hdk[i][2];
				    
				    for (int ii = 0; ii < tablaRawData.size(); ii++) {
				         xTemp[ii]=0.0;
				         
				    }
				    xTemp[0] = b[0]*bayanRawData.get(0);
				    xTemp[1] = b[0]*bayanRawData.get(1)+b[1]*bayanRawData.get(0) - a[1]*xTemp[0];
				    xTemp[2] = b[0]*bayanRawData.get(2)+b[1]*bayanRawData.get(1)+b[2]*bayanRawData.get(0) - a[1]*xTemp[1]
				    		- a[2]*xTemp[0];
				    
				    for (int iii = 3; iii < bayanRawData.size(); iii++) {			    	
				    	xTemp[iii]=b[0]*bayanRawData.get(iii)+b[1]*bayanRawData.get(iii-1)+b[2]*bayanRawData.get(iii-2)
				    			-a[1]*xTemp[iii-1]-a[2]*xTemp[iii-2];
				    			
				    }
				    for (int ii = 0; ii < tablaRawData.size(); ii++) {
				    	bayanRawData.set(ii,xTemp[ii]);
				    }
				}
			    
			   
			}*/
			catch (Exception e) {
				e.printStackTrace();
			}
			return bayanRawData;
		}
		
		
		private static double[][][] ERBFilters (Double samplingRate, int noChannels, Double lowestFreq) {
			/*ArrayList<Double> cf= new ArrayList<Double>();
			ArrayList<Double> ERB= new ArrayList<Double>();
			ArrayList<Double> B= new ArrayList<Double>();
			ArrayList<Double> B1= new ArrayList<Double>();
			ArrayList<Double> B2= new ArrayList<Double>();
			ArrayList<Double> B2= new ArrayList<Double>();*/
			
			
			Double cf= (double) 0;
			Double ERB= (double) 0;
			Double B= (double) 0;
			Double B0= (double) 1;
			//Double B1= (double) 1;
			//Double B2= (double) 0;
			Double A0= (double) 0;
			Double A2= (double) 0;
			//Double A11= (double) 0;
			//Double A12= (double) 0;
			//Double A13= (double) 0;
			//Double A14= (double) 0;
			
			//int chan = (int) 0;
			double[][][] multi = new double[noChannels][4][6];
			//double[] B = new double[noChannels];
			double[] B1 = new double[noChannels];
			double[] B2 = new double[noChannels];
			double[] A11 = new double[noChannels];
			double[] A12 = new double[noChannels];
			double[] A13 = new double[noChannels];
			double[] A14 = new double[noChannels];
			double[] gainAbs = new double[noChannels];
			
			Complex gain ;
			//Double gainAbs ;
			
			Double T = (double) (1/samplingRate);
			Double EarQ = (double) 9.26449;				//  Glasberg and Moore Parameters
			Double minBW = (double) 24.7;
			A0 = T;
			try {		
				
				for (int i=1;i<=noChannels;i++) {
					cf = -(EarQ*minBW)+(Math.exp(i*(-Math.log((samplingRate/2)+EarQ*minBW)+
							Math.log(lowestFreq+ EarQ*minBW))/noChannels)*(samplingRate/2 + EarQ*minBW));
					//cf.add(valueCalc);
					//cfCurr = valueCalc;
					ERB = (cf/EarQ) + minBW;
					B = 1.019*2*Math.PI*ERB;
					B1[i-1] = -2*Math.cos(2*cf*Math.PI*T)/Math.exp(B*T);
					B2[i-1] = Math.exp(-2*B*T);
					
					A11[i-1] = -(2*T*Math.cos(2*cf*Math.PI*T)/Math.exp(B*T) + 
							2*Math.sqrt(3+Math.pow(2, 1.5))*T*Math.sin(2*cf*Math.PI*T)/
							Math.exp(B*T))/2;
					
					A12[i-1] = -(2*T*Math.cos(2*cf*Math.PI*T)/Math.exp(B*T) - 
							2*Math.sqrt(3+Math.pow(2, 1.5))*T*Math.sin(2*cf*Math.PI*T)/
							Math.exp(B*T))/2;
					
					A13[i-1] = -(2*T*Math.cos(2*cf*Math.PI*T)/Math.exp(B*T) + 
							2*Math.sqrt(3-Math.pow(2, 1.5))*T*Math.sin(2*cf*Math.PI*T)/
							Math.exp(B*T))/2;
					
					A14[i-1] = -(2*T*Math.cos(2*cf*Math.PI*T)/Math.exp(B*T) - 
							2*Math.sqrt(3-Math.pow(2, 1.5))*T*Math.sin(2*cf*Math.PI*T)/
							Math.exp(B*T))/2;
					
					Complex c = new Complex(Math.cos(4*cf*Math.PI*T),Math.sin(4*cf*Math.PI*T));
					Complex d = new Complex(Math.cos(2*cf*Math.PI*T),Math.sin(2*cf*Math.PI*T));
					
					//temp = Math.sqrt(3 - 2*Math.sqrt(2));
				    //temp = Math.cos(2*cf*Math.PI*T)-Math.sqrt(3 - 2*Math.sqrt(2))*Math.sin(2*cf*Math.PI*T);
					
					gain = c.multiply(-2*T).add(d.multiply(Math.exp(-(B*T))).multiply(2*T).multiply(Math.cos(2*cf*Math.PI*T)- 
							Math.sqrt(3 - 2*Math.sqrt(2))*Math.sin(2*cf*Math.PI*T))).
							 multiply(c.multiply(-2*T).add(d.multiply(Math.exp(-(B*T))).multiply(2*T).multiply(Math.cos(2*cf*Math.PI*T)+ 
							Math.sqrt(3 - 2*Math.sqrt(2))*Math.sin(2*cf*Math.PI*T)))).
							 multiply(c.multiply(-2*T).add(d.multiply(Math.exp(-(B*T))).multiply(2*T).multiply(Math.cos(2*cf*Math.PI*T)- 
							Math.sqrt(3 + 2*Math.sqrt(2))*Math.sin(2*cf*Math.PI*T)))).
							 multiply(c.multiply(-2*T).add(d.multiply(Math.exp(-(B*T))).multiply(2*T).multiply(Math.cos(2*cf*Math.PI*T)+ 
							Math.sqrt(3 + 2*Math.sqrt(2))*Math.sin(2*cf*Math.PI*T)))).
							divide((c.multiply(-2).add(-2/Math.exp(2*B*T)).add(2/Math.exp(B*T)).add(c.multiply(2/Math.exp(B*T)))).pow(4));
			        gainAbs[i-1] = gain.abs(); 
				}
				int i = 0;
				for (int chan=noChannels-1;chan>=0;chan--) {
			        
			        multi[i][0][0] = A0/gainAbs[chan];
			        multi[i][0][1] = A11[chan]/gainAbs[chan];
			        multi[i][0][2] = A2/gainAbs[chan];
			        multi[i][0][3] = B0;
			        multi[i][0][4] = B1[chan];
			        multi[i][0][5] = B2[chan];
			        
			        multi[i][1][0] = A0;
			        multi[i][1][1] = A12[chan];
			        multi[i][1][2] = A2;
			        multi[i][1][3] = B0;
			        multi[i][1][4] = B1[chan];
			        multi[i][1][5] = B2[chan];
			        
			        multi[i][2][0] = A0;
			        multi[i][2][1] = A13[chan];
			        multi[i][2][2] = A2;
			        multi[i][2][3] = B0;
			        multi[i][2][4] = B1[chan];
			        multi[i][2][5] = B2[chan];
			        
			        multi[i][3][0] = A0;
			        multi[i][3][1] = A14[chan];
			        multi[i][3][2] = A2;
			        multi[i][3][3] = B0;
			        multi[i][3][4] = B1[chan];
			        multi[i][3][5] = B2[chan];
			        
			        i = i+1;
				} 
			
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return multi;
		}
}
