/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import org.bhaduri.generatecall.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import org.bhaduri.datatransfer.DTO.CsvTickData;
import org.bhaduri.datatransfer.DTO.DataStoreNames;
import static org.bhaduri.datatransfer.DTO.DataStoreNames.TICKER_DATA_NIFTY;
import org.bhaduri.datatransfer.DTO.RecordCallPrice;
import org.bhaduri.datatransfer.DTO.RecordMinute;
import org.bhaduri.datatransfer.DTO.ResultData;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author sb
 */
@Named(value = "generatedCall")
@ViewScoped
@ManagedBean
public class GeneratedCall implements Serializable {

    /**
     * Creates a new instance of GenerateLastCall
     */
    private LineChartModel lineModel;
    private String infoText;
        
    @PostConstruct
    public void init() {
        populateNifty();
        generatIntradayCall();
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public String getInfoText() {
        return infoText;
    }
     
    
//    public LineChartModel generatIntradayCall() {
    public String generatIntradayCall() {
        File directory = new File(DataStoreNames.TICKER_DATA_DETAILS);
//        List listFileArray = Arrays.asList(directory.list());
//        Collections.sort(listFileArray); //directories are sorted as per their name
//        int dirCount = listFileArray.size();
        String scripFolderPath = "";
        String[] delimitedString;
        List<RecordCallPrice> resultDatas = new ArrayList<RecordCallPrice>(); // call list for the last and 
//        previous days file using elliot curve algo
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDate = "";
//        for (int i = 0; i < dirCount; i++) {
//            scripFolderPath = DataStoreNames.TICKER_DATA_DETAILS.concat(listFileArray.get(i).toString());
//            scripFolderPath = scripFolderPath.concat("/");
//            File fileListPerScrip = new File(scripFolderPath);
//            File[] arrayPerScrip = fileListPerScrip.listFiles();
//            System.out.println(arrayPerScrip[0]);
//            
//            String scripId = listFileArray.get(i).toString();
//            String scripLast = arrayPerScrip[0].getAbsolutePath();
//            CsvTickData recordDataLast = new CsvTickData();
//            recordDataLast = readCSVData(scripLast);
////            formattedDate = targetFormat.format(recordDataLast.getDateTime());
//            resultDatas.add(fillResult(recordDataLast.getTickData(), scripId, recordDataLast.getDateTime()));            
//        }
        scripFolderPath = DataStoreNames.TICKER_DATA_DETAILS.concat("NIFTY 50");
        scripFolderPath = scripFolderPath.concat("/");
        File fileListPerScrip = new File(scripFolderPath);
        File[] arrayPerScrip = fileListPerScrip.listFiles();
//        System.out.println(arrayPerScrip[0]);

        String scripId = "NIFTY 50";
        String scripLast = arrayPerScrip[0].getAbsolutePath();
        CsvTickData recordDataLast = new CsvTickData();
        recordDataLast = readCSVData(scripLast);
        formattedDate = targetFormat.format(recordDataLast.getDateTime());
        RecordCallPrice niftyResult = new RecordCallPrice();
        niftyResult = fillResult(recordDataLast.getTickData(), scripId, recordDataLast.getDateTime());

//        lineModel = populateNifty(scripId, niftyResult.getLastCallVersionOne(), 
//                niftyResult.getPrice(), formattedDate);
        infoText= "ScripID="+scripId+", CallOne="+niftyResult.getLastCallVersionOne()+","
                + " Calltime="+formattedDate;
        
        return infoText;
    }
    private RecordMinute createMinuteDataRec(String lineFromFile) {
        RecordMinute record = new RecordMinute();
        String[] fields = null;
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        fields = lineFromFile.split(",");
        record.setScripID(fields[0]);
        try {
            record.setLastUpdateTime(targetFormat.parse(fields[1]));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        record.setOpenprice(Double.valueOf(fields[2]));
        record.setDaylastprice(Double.valueOf(fields[3]));
        record.setDayhighprice(Double.valueOf(fields[4]));
        record.setDaylowprice(Double.valueOf(fields[5]));
        record.setPrevcloseprice(Double.valueOf(fields[6]));
        record.setTotaltradedvolume(Double.valueOf(fields[7]));

        return record;
    }
    
    private CsvTickData readCSVData(String csvPath) {
        CsvTickData retCsvTickData = new CsvTickData();
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        String line;
        double index = 1;
        List<List<Double>> recordData = new ArrayList<>();
        List<Double> row = new ArrayList<>();
        String[] fields = null;
        try {
            BufferedReader brPrev = new BufferedReader(new FileReader(csvPath));
            line = brPrev.readLine();
            while ((line = brPrev.readLine()) != null) {
                row = new ArrayList<>();
                // use comma as separator  
                fields = line.split(",");
                //fields will now contain all values    
                row.add(index);
                row.add(Double.valueOf(fields[3]));

                recordData.add(row);
                index = index + 1;
            }
            retCsvTickData.setTickData(recordData);
            try {
                retCsvTickData.setDateTime(targetFormat.parse(fields[1]));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return retCsvTickData;
    }
    
    private RecordCallPrice fillResult(List<List<Double>> recordData, String scripId, Date lastUpdateDate) {

        int callCount = 3;
        Smoothing smoothing = new Smoothing(recordData, callCount);
        List<SmoothData> smoothData = new ArrayList<SmoothData>();
        smoothData = smoothing.genCall();
        RecordCallPrice eachResultData = new RecordCallPrice();
        Double thresHold;
        thresHold = (0.5 / 100) * (recordData.get(recordData.size() - 1).get(1))
                + ((0.5 / 100) * recordData.get(recordData.size() - 1).get(1)) * 18 / 100;

        eachResultData.setScripID(scripId);//to be fixed        
        eachResultData.setLastUpdateTime(lastUpdateDate);//to be fixed
        eachResultData.setPrice(recordData.get(recordData.size() - 1).get(1));
        eachResultData.setLastCallVersionOne(smoothData.get(2).getCallArrayOne());
        eachResultData.setLastCallVersionTwo(smoothData.get(2).getCallArrayTwo());
        eachResultData.setTallyVersionOne("");
        eachResultData.setTallyVersionTwo("");
        eachResultData.setRetraceVersionOne(smoothData.get(2).getRetraceOne());
        eachResultData.setRetraceVersionTwo(smoothData.get(2).getRetraceTwo());
        
        if (smoothData.get(2).getCallArrayOne().equals("buy")) {
            eachResultData.setPriceBrokerageGstOne(recordData.get(recordData.size() - 1).get(1) + thresHold);
        } else {
            if (smoothData.get(2).getCallArrayOne().equals("sell")) {
                eachResultData.setPriceBrokerageGstOne(recordData.get(recordData.size() - 1).get(1) - thresHold);
            } else {
                eachResultData.setPriceBrokerageGstOne(recordData.get(recordData.size() - 1).get(1));
            }
        }

        if (smoothData.get(2).getCallArrayTwo().equals("buy")) {
            eachResultData.setPriceBrokerageGstTwo(recordData.get(recordData.size() - 1).get(1) + thresHold);
        } else {
            if (smoothData.get(2).getCallArrayTwo().equals("sell")) {
                eachResultData.setPriceBrokerageGstTwo(recordData.get(recordData.size() - 1).get(1) - thresHold);
            } else {
                eachResultData.setPriceBrokerageGstTwo(recordData.get(recordData.size() - 1).get(1));
            }
        }

//        System.out.println("recordTest:" + records.get(records.size() - 1).get(1));
        return eachResultData;
    }
    
//    public LineChartModel populateNifty(String scripID, String call, Double price, String lastUpdTime){  
    public LineChartModel populateNifty(){

        String fileName = TICKER_DATA_NIFTY;
        List<RecordMinute> minuteDataForInterval = new ArrayList<>();
        RecordMinute record = new RecordMinute();
        String formattedDate;        
        String line;
        
        try {
            BufferedReader brPrev = new BufferedReader(new FileReader(fileName));  
            line = brPrev.readLine();
            while ((line = brPrev.readLine()) != null) {
                record = createMinuteDataRec(line);
                minuteDataForInterval.add(record);
                record = new RecordMinute();

            }
        } catch (IOException ex) {
            Logger.getLogger(LandingNifty.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        
        List<String> labels = new ArrayList<>();
        
        for (int k = minuteDataForInterval.size()-51; k < minuteDataForInterval.size(); k++) {
            values.add(Double.toString(minuteDataForInterval.get(k).getDaylastprice()));
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            formattedDate = targetFormat.format(minuteDataForInterval.get(k).getLastUpdateTime());
            labels.add(formattedDate);
        }
        //value for datatip
//        values.add(Double.toString(price));
//        labels.add(scripID+", "+call+", "+lastUpdTime);
        
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Nifty 50 Chart");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);
        
        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
//        options.setMaintainAspectRatio(false);
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Nifty 50 Chart");
        options.setTitle(title);

        Title subtitle = new Title();
        subtitle.setDisplay(true);
//        subtitle.setText("ScripID="+scripID+", CallOne="+call+", Calltime="+lastUpdTime);
        subtitle.setText("Last Nifty 50 Data Details");
        options.setTitle(subtitle);

        lineModel.setOptions(options);
        lineModel.setData(data);
        return lineModel;
    }

}
