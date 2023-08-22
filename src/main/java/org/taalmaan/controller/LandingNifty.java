/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.bhaduri.datatransfer.DTO.*;
//import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author sb
 */
@Named(value = "landingNifty")
@ViewScoped
@ManagedBean
public class LandingNifty implements Serializable {

    private LineChartModel lineModel;

    /**
     * Creates a new instance of LandingNifty
     */
//    public void setLineModel(LineChartModel lineModel) {
//        this.lineModel = lineModel;
//    }

    @PostConstruct
    public void init() {
        populateNifty();
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public LineChartModel populateNifty(){
//        lastNifty = "50";
//        values.add(65);
//        values.add(59);
//        values.add(80);
//        values.add(81);
//        values.add(56);
//        values.add(55);
//        values.add(40);
        

//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");
//        labels.add("July");
        

        String fileName = "/home/sb/Documents/testi_midday/mid_csv/tarang_23Aug_m.csv";
        List<RecordMinute> minuteDataForInterval = new ArrayList<>();
        RecordMinute record = new RecordMinute();
        String formattedDate;        
        String line;
        
        try {
//            object = new ReversedLinesFileReader(file); 
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
        
        for (int k = minuteDataForInterval.size()-11; k < minuteDataForInterval.size(); k++) {
            values.add(Double.toString(minuteDataForInterval.get(k).getDaylastprice()));
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            formattedDate = targetFormat.format(minuteDataForInterval.get(k).getLastUpdateTime());
            labels.add(formattedDate);
        }
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("My First Dataset");
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
        subtitle.setText("Line Chart Subtitle");
        options.setTitle(subtitle);

        lineModel.setOptions(options);
        lineModel.setData(data);
        return lineModel;
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
}
