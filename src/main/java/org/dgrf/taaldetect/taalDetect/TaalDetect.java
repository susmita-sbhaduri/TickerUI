package org.dgrf.taaldetect.taalDetect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dgrf.taaldetect.coOccur.CalculateCooccur;
import org.dgrf.taaldetect.taalUtill.TaalFileHandler;

import org.dgrf.taalmaanservices.TaalMaanServices.SongDataService;
import org.dgrf.wavread.driver.ReadAudio;

public class TaalDetect {

    private String inputWav;
    private String outputTala;
    private double outputTempo;


    public TaalDetect(String inputWav) {
        this.inputWav = inputWav;
        setDefaultParamFilePath();
    }

    private void setDefaultParamFilePath() {

    }
    private String getParamfilePath(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(resourceName);
        File file = new File(resourceName);
        try {

            OutputStream out;

            byte buf[] = new byte[1024];
            int len;
            out = new FileOutputStream(file);
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(TaalDetect.class.getName()).log(Level.SEVERE, null, ex);
        }

        return(file.getAbsolutePath());
    }

    public void calcTaalaTempo() throws UnsupportedEncodingException {
        String inputText = null;
        int[][] coOccurMatrix = new int[16][16];
        double[][] percussiveDurList;
        int[][] percussiveCountList;
        int lengthArray = 0;
        int maxCoocValue = 0;
        int[][] maxRowCol;

        int[][] dadraParmArray;

        int[][] kaharbaParmArray;

        int[][] rupakParmArray;

        int[][] bhajaniParmArray;
        String taala = "none";
        String taalaOp = null;
        int[][] matraArray = new int[100][3];
        int k = 0;
        int matraIndex = 0;
        double opTempo = 0;

        ArrayList<Double> songTextArr = new ArrayList<Double>();
        ArrayList<Double> pTempDur1 = new ArrayList<Double>();
        ArrayList<Double> pTempDur2 = new ArrayList<Double>();
        ArrayList<Integer> firstPerStrokeCount = new ArrayList<Integer>();
        ArrayList<Integer> secondPerStrokeCount = new ArrayList<Integer>();

        File fd = new File(this.inputWav);
        ReadAudio rWAV = new ReadAudio(fd);
        try {
            rWAV.readAudioIntoText();
        } catch (IOException ex) {
            Logger.getLogger(TaalDetect.class.getName()).log(Level.SEVERE, null, ex);
        }
        inputText = rWAV.getOutFname();

        songTextArr = TaalFileHandler.readRawDataFile(inputText);
        CalculateCooccur calcCooccur = new CalculateCooccur(songTextArr);
        lengthArray = calcCooccur.getLengthArray();
        percussiveDurList = new double[lengthArray][2];
        percussiveCountList = new int[lengthArray][2];
        percussiveCountList = calcCooccur.getPercussiveCountList();
        percussiveDurList = calcCooccur.getPercussiveDurList();

        coOccurMatrix = calcCooccur.getCoOccurMatrix();
        maxRowCol = new int[coOccurMatrix.length][2];

        try {
            maxCoocValue = TaalFileHandler.findMaxValMatrix(coOccurMatrix, coOccurMatrix.length, coOccurMatrix.length);
            maxRowCol = TaalFileHandler.findMaxValIndex(coOccurMatrix, coOccurMatrix.length, coOccurMatrix.length,
                    maxCoocValue);

            for (int i = 0; i < maxRowCol.length; i++) {
                firstPerStrokeCount.add(maxRowCol[i][0] + 1);
                secondPerStrokeCount.add(maxRowCol[i][1] + 1);
                coOccurMatrix[maxRowCol[i][0]][maxRowCol[i][1]] = 0;
            }
            maxCoocValue = TaalFileHandler.findMaxValMatrix(coOccurMatrix, coOccurMatrix.length, coOccurMatrix.length);
            maxRowCol = TaalFileHandler.findMaxValIndex(coOccurMatrix, coOccurMatrix.length, coOccurMatrix.length,
                    maxCoocValue);

            for (int i = 0; i < maxRowCol.length; i++) {
                firstPerStrokeCount.add(maxRowCol[i][0] + 1);
                secondPerStrokeCount.add(maxRowCol[i][1] + 1);
            }

            for (int i = 0; i < firstPerStrokeCount.size(); i++) {
                if ((firstPerStrokeCount.get(i) == 6) && (secondPerStrokeCount.get(i) == 6)) {
                    matraArray[k][0] = 6;
                    matraArray[k][1] = 6;
                    matraArray[k][2] = 6;
                    k = k + 1;
                    taala = "done";
                    continue;
                }
                if ((firstPerStrokeCount.get(i) == 8) && (secondPerStrokeCount.get(i) == 8)) {
                    matraArray[k][0] = 8;
                    matraArray[k][1] = 8;
                    matraArray[k][2] = 8;
                    k = k + 1;
                    taala = "done";
                    continue;
                }
                if (((firstPerStrokeCount.get(i) == 10) && (secondPerStrokeCount.get(i) == 4))
                        || ((firstPerStrokeCount.get(i) == 4) && (secondPerStrokeCount.get(i) == 10))) {
                    matraArray[k][0] = 14;
                    matraArray[k][1] = firstPerStrokeCount.get(i);
                    matraArray[k][2] = secondPerStrokeCount.get(i);
                    k = k + 1;
                    taala = "done";
                    continue;
                }
                if (((firstPerStrokeCount.get(i) == 13) && (secondPerStrokeCount.get(i) == 3))
                        || ((firstPerStrokeCount.get(i) == 3) && (secondPerStrokeCount.get(i) == 13))) {
                    matraArray[k][0] = 16;
                    matraArray[k][1] = firstPerStrokeCount.get(i);
                    matraArray[k][2] = secondPerStrokeCount.get(i);
                    k = k + 1;
                    taala = "done";
                    continue;
                }
            }

//            dadraParmArray = TaalFileHandler.readCsvDataFile(dadraParmFileName);
//            kaharbaParmArray = TaalFileHandler.readCsvDataFile(kaharbaParmFileName);
//            rupakParmArray = TaalFileHandler.readCsvDataFile(rupakParmFileName);
//            bhajaniParmArray = TaalFileHandler.readCsvDataFile(bhajaniParmFileName);
            SongDataService sd = new SongDataService();
            dadraParmArray = sd.getTaalParams("dadra");
            kaharbaParmArray = sd.getTaalParams("kaharba");
            rupakParmArray = sd.getTaalParams("rupak");
            bhajaniParmArray = sd.getTaalParams("bhajani");

            if (taala.equals("none")) {
                for (int j = 0; j < firstPerStrokeCount.size(); j++) {

                    if (!taala.equals("dadradone")) {
                        for (int i = 0; i < dadraParmArray.length; i++) {
                            if ((firstPerStrokeCount.get(j) == dadraParmArray[i][0])
                                    && (secondPerStrokeCount.get(j) == dadraParmArray[i][1])) {
                                matraArray[k][0] = 6;
                                matraArray[k][1] = firstPerStrokeCount.get(j);
                                matraArray[k][2] = secondPerStrokeCount.get(j);
                                k = k + 1;
                                taala = "dadradone";
                                break;
                            }
                        }
                    }

                    if (!taala.equals("kaharbadone")) {
                        for (int i = 0; i < kaharbaParmArray.length; i++) {
                            if ((firstPerStrokeCount.get(j) == kaharbaParmArray[i][0])
                                    && (secondPerStrokeCount.get(j) == kaharbaParmArray[i][1])) {
                                matraArray[k][0] = 8;
                                matraArray[k][1] = firstPerStrokeCount.get(j);
                                matraArray[k][2] = secondPerStrokeCount.get(j);
                                k = k + 1;
                                taala = "kaharbadone";
                                break;
                            }
                        }
                    }

                    if (!taala.equals("rupakdone")) {
                        for (int i = 0; i < rupakParmArray.length; i++) {
                            if ((firstPerStrokeCount.get(j) == rupakParmArray[i][0])
                                    && (secondPerStrokeCount.get(j) == rupakParmArray[i][1])) {
                                matraArray[k][0] = 14;
                                matraArray[k][1] = firstPerStrokeCount.get(j);
                                matraArray[k][2] = secondPerStrokeCount.get(j);
                                k = k + 1;
                                taala = "rupakdone";
                                break;
                            }
                        }
                    }

                    if (!taala.equals("bhajanidone")) {
                        for (int i = 0; i < bhajaniParmArray.length; i++) {
                            if ((firstPerStrokeCount.get(j) == bhajaniParmArray[i][0])
                                    && (secondPerStrokeCount.get(j) == bhajaniParmArray[i][1])) {
                                matraArray[k][0] = 16;
                                matraArray[k][1] = firstPerStrokeCount.get(j);
                                matraArray[k][2] = secondPerStrokeCount.get(j);
                                k = k + 1;
                                taala = "bhajanidone";
                                break;
                            }
                        }
                    }

                }
            }
            int[][] matraArrayUpdated = new int[k][3];
            for (int j = 0; j < k; j++) {
                matraArrayUpdated[j][0] = matraArray[j][0];
                matraArrayUpdated[j][1] = matraArray[j][1];
                matraArrayUpdated[j][2] = matraArray[j][2];
            }
            if (taala.equals("none")) {
                this.outputTala = "none";
                this.outputTempo = 0.0;
            } else {
                double matra = Math.abs(matraArrayUpdated[0][0]);
                
                for (int i = 0; i < matraArrayUpdated.length; i++) {
                    if (matra < Math.abs(matraArrayUpdated[i][0])) {
                        matra = Math.abs(matraArrayUpdated[i][0]);
                        matraIndex = i;
                    }
                }
                for (int i = 0; i < percussiveCountList.length; i++) {
                    if ((percussiveCountList[i][0] == matraArrayUpdated[matraIndex][1])
                            && (percussiveCountList[i][1] == matraArrayUpdated[matraIndex][2])) {
                        pTempDur1.add(percussiveDurList[i][0]);
                        pTempDur2.add(percussiveDurList[i][1]);
                    }
                }
                opTempo = 60
                        / ((TaalFileHandler.findMeanValue(pTempDur1) + TaalFileHandler.findMeanValue(pTempDur2)) / 2);
                if (matra == 6) {
                    taalaOp = "Dadra";
                }
                if (matra == 8) {
                    taalaOp = "Kaharba";
                }
                if (matra == 14) {
                    taalaOp = "Rupak";
                }
                if (matra == 16) {
                    taalaOp = "Bhajani";
                }

                this.outputTala = taalaOp;
                this.outputTempo = opTempo;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String getTaala() {
        return outputTala;
    }

    public double getTempo() {
        return outputTempo;
    }

//    public String getDadraParmFileName() {
//        return dadraParmFileName;
//    }
//
//    public void setDadraParmFileName(String dadraParmFileName) {
//        this.dadraParmFileName = dadraParmFileName;
//    }
//
//    public String getKaharbaParmFileName() {
//        return kaharbaParmFileName;
//    }
//
//    public void setKaharbaParmFileName(String kaharbaParmFileName) {
//        this.kaharbaParmFileName = kaharbaParmFileName;
//    }
//
//    public String getRupakParmFileName() {
//        return rupakParmFileName;
//    }
//
//    public void setRupakParmFileName(String rupakParmFileName) {
//        this.rupakParmFileName = rupakParmFileName;
//    }
//
//    public String getBhajaniParmFileName() {
//        return bhajaniParmFileName;
//    }
//
//    public void setBhajaniParmFileName(String bhajaniParmFileName) {
//        this.bhajaniParmFileName = bhajaniParmFileName;
//    }

}
