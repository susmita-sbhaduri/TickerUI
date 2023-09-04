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
import java.util.Locale;
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
import org.bhaduri.datatransfer.DTO.ScripID;
import org.bhaduri.minutedataaccess.services.MasterDataServices;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author sb
 */
@Named(value = "validationsummary")
@ViewScoped
@ManagedBean
public class ValidationSummary implements Serializable {

    /**
     * Creates a new instance of ValidateCall
     */
    private RecordCallPrice selectedAllParm;
    private String scripId;
    private String callTwoFirst;
    private String lastUpdateFirst;
    private String callTwoSecond;
    private String lastUpdateSecond;
    private List<RecordCallPrice> reverseCallList;
        
    @PostConstruct
    public void init() {
        listDetails();
    }

    public String getScripId() {
        return scripId;
    }

    public void setScripId(String scripId) {
        this.scripId = scripId;
    }

    public String getCallTwoFirst() {
        return callTwoFirst;
    }

    public void setCallTwoFirst(String callTwoFirst) {
        this.callTwoFirst = callTwoFirst;
    }

    public String getLastUpdateFirst() {
        return lastUpdateFirst;
    }

    public void setLastUpdateFirst(String lastUpdateFirst) {
        this.lastUpdateFirst = lastUpdateFirst;
    }
       

    public List<RecordCallPrice> getReverseCallList() {
        return reverseCallList;
    }

    public void setReverseCallList(List<RecordCallPrice> reverseCallList) {
        this.reverseCallList = reverseCallList;
    }

    public RecordCallPrice getSelectedAllParm() {
        return selectedAllParm;
    }

    public void setSelectedAllParm(RecordCallPrice selectedAllParm) {
        this.selectedAllParm = selectedAllParm;
    }

   
    public String getCallTwoSecond() {
        return callTwoSecond;
    }

    public void setCallTwoSecond(String callTwoSecond) {
        this.callTwoSecond = callTwoSecond;
    }

    public String getLastUpdateSecond() {
        return lastUpdateSecond;
    }

    public void setLastUpdateSecond(String lastUpdateSecond) {
        this.lastUpdateSecond = lastUpdateSecond;
    }
    
    
    
    public void listDetails() {
//        scripIDList = new ArrayList<>();
//        List<String> scripListTemp = new ArrayList<>();
        MasterDataServices masterDataService = new MasterDataServices();
//        scripListTemp = masterDataService.readScripData();
        DateFormat originalFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
        
        if (callTwoSelected.equals("buy")) {
            try {
                Date dateselected = originalFormat.parse(lastUpdateSelected);
                reverseCallList = masterDataService.listReverseCalls(scripIDSelected,
                        dateselected, "sell");
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        
        if (callTwoSelected.equals("sell")) {
            try {
                Date dateselected = originalFormat.parse(lastUpdateSelected);
                reverseCallList = masterDataService.listReverseCalls(scripIDSelected,
                        dateselected, "buy");
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
//        for (int k = 0; k < scripListTemp.size(); k++) {
//            ScripID scripID = new ScripID();
//            scripID.setScripID(scripListTemp.get(k));
//            scripIDList.add(scripID);
//        }
        System.out.println("scripIDSelected");
        
    }
    public String populateAllParms() {
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        lastUpdateSecond = targetFormat.format(selectedAllParm.getLastUpdateTime());
        
        scripIDSecond = selectedAllParm.getScripID();
        callTwoSecond = selectedAllParm.getLastCallVersionTwo();
//        System.out.println("Trying to navigate to " + scripID);
        return "ValidationSummary";
    }

}
