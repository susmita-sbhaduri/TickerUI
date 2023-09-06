/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.bhaduri.datatransfer.DTO.RecordCallPrice;
import org.bhaduri.minutedataaccess.services.MasterDataServices;

/**
 *
 * @author sb
 */
@Named(value = "validateBuyCall")
@ViewScoped
@ManagedBean
public class ValidateBuyCall implements Serializable {

    /**
     * Creates a new instance of ValidateCall
     */
    private RecordCallPrice selectedAllParm;
    private String scripIDSecond;
    private String callTwoSecond;
    private String lastUpdateSecond;
    private String scripIDSelected;
    private String callTwoSelected;
    private String lastUpdateSelected;
    private String priceSeond;
    private List<RecordCallPrice> reverseCallList;
    private List<RecordCallPrice> revCallListAll;
    private String callListLen;
        
    @PostConstruct
    public void init() {
//        listDetails();
    }
    
    public String getScripIDSelected() {
        return scripIDSelected;
    }

    public void setScripIDSelected(String scripIDSelected) {
        this.scripIDSelected = scripIDSelected;
    }

    public String getCallTwoSelected() {
        return callTwoSelected;
    }

    public void setCallTwoSelected(String callTwoSelected) {
        this.callTwoSelected = callTwoSelected;
    }

    public String getLastUpdateSelected() {
        return lastUpdateSelected;
    }

    public void setLastUpdateSelected(String lastUpdateSelected) {
        this.lastUpdateSelected = lastUpdateSelected;
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

    public String getScripIDSecond() {
        return scripIDSecond;
    }

    public void setScripIDSecond(String scripIDSecond) {
        this.scripIDSecond = scripIDSecond;
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

    public String getCallListLen() {
        return callListLen;
    }

    public void setCallListLen(String callListLen) {
        this.callListLen = callListLen;
    }

    public String getPriceSeond() {
        return priceSeond;
    }

    public void setPriceSeond(String priceSeond) {
        this.priceSeond = priceSeond;
    }

    public List<RecordCallPrice> getRevCallListAll() {
        return revCallListAll;
    }

    public void setRevCallListAll(List<RecordCallPrice> revCallListAll) {
        this.revCallListAll = revCallListAll;
    }
    
       
    public void listDetails() {
        MasterDataServices masterDataService = new MasterDataServices();
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        
        if (callTwoSelected.equals("buy")) {
            try {
                Date dateselected = originalFormat.parse(lastUpdateSelected);
                revCallListAll = masterDataService.listReverseCalls(scripIDSelected,
                        dateselected, "sell");
                reverseCallList = new ArrayList<>();
                for (int k = 0; k < 10; k++) {
                    reverseCallList.add(revCallListAll.get(k));
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        
        if (callTwoSelected.equals("sell")) {
            try {
                Date dateselected = originalFormat.parse(lastUpdateSelected);
                revCallListAll = masterDataService.listReverseCalls(scripIDSelected,
                        dateselected, "buy");
                reverseCallList = new ArrayList<>();
                for (int k = 0; k < 10; k++) {
                    reverseCallList.add(revCallListAll.get(k));
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("scripIDSelected");
        
    }
    public String populateAllParms() {
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        lastUpdateSecond = targetFormat.format(selectedAllParm.getLastUpdateTime());
        
        scripIDSecond = selectedAllParm.getScripID();
        callTwoSecond = selectedAllParm.getLastCallVersionTwo();
        callListLen = String.valueOf(reverseCallList.size());
        priceSeond = String.valueOf(selectedAllParm.getPrice());
//        System.out.println("Trying to navigate to " + scripID);
        return "ValidationSummary";
    }

}
