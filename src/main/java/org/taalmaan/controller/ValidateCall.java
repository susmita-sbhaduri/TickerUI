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
@Named(value = "validateCall")
@ViewScoped
@ManagedBean
public class ValidateCall implements Serializable {

    /**
     * Creates a new instance of ValidateCall
     */
    private List<ScripID> scripIDList;
    private ScripID selectedScripID;
    private String scripID;
        
    @PostConstruct
    public void init() {
        listScriptId();
    }

    public List<ScripID> getScripIDList() {
        return scripIDList;
    }  

    public ScripID getSelectedScripID() {
        return selectedScripID;
    }

    public void setSelectedScripID(ScripID selectedScripID) {
        this.selectedScripID = selectedScripID;
    }

    public String getScripID() {
        return scripID;
    }

    public void setScripID(String scripID) {
        this.scripID = scripID;
    }
    
    public void listScriptId() {
        scripIDList = new ArrayList<>();
        List<String> scripListTemp = new ArrayList<>();
        MasterDataServices masterDataService = new MasterDataServices();
        scripListTemp = masterDataService.readScripData();
        for (int k = 0; k < scripListTemp.size(); k++) {
            ScripID scripID = new ScripID();
            scripID.setScripID(scripListTemp.get(k));
            scripIDList.add(scripID);
        }
        
    }
    public String showScripChart() {
        
        scripID = selectedScripID.getScripID();
        System.out.println("Trying to navigate to " + scripID);
        return "ScripChart";
    }

}
