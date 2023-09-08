/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.bhaduri.datatransfer.DTO.RecordCallPrice;
import org.bhaduri.minutedataaccess.services.MasterDataServices;

/**
 *
 * @author sb
 */
@Named(value = "scripchart")
@ViewScoped
public class Scripchart implements Serializable {
    private String scripID;
    private List<RecordCallPrice> buyPerScripid;
    private List<RecordCallPrice> sellPerScripid;
    private RecordCallPrice selectedParm;
    private String scripIDSelected;
    private String callTwoSelected;
    private String lastUpdateSelected;
    
    @PostConstruct
    public void init() {
//        listDetails();
    }
  
    public String getScripID() {
        return scripID;
    }

    public void setScripID(String scripID) {
        this.scripID = scripID;
    }

    public List<RecordCallPrice> getBuyPerScripid() {
        return buyPerScripid;
    }

    public void setBuyPerScripid(List<RecordCallPrice> buyPerScripid) {
        this.buyPerScripid = buyPerScripid;
    }

    public List<RecordCallPrice> getSellPerScripid() {
        return sellPerScripid;
    }

    public void setSellPerScripid(List<RecordCallPrice> sellPerScripid) {
        this.sellPerScripid = sellPerScripid;
    }

    public RecordCallPrice getSelectedParm() {
        return selectedParm;
    }

    public void setSelectedParm(RecordCallPrice selectedParm) {
        this.selectedParm = selectedParm;
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
    
    public void loadScripCalls() {
        buyPerScripid = new ArrayList<>();
        sellPerScripid = new ArrayList<>();
        List<RecordCallPrice> callsPerScripid;
        int buy_count =0;
        int sell_count =0;
        
        MasterDataServices masterDataService = new MasterDataServices();
        callsPerScripid = masterDataService.callListPerScrip(scripID);
        for (int k = callsPerScripid.size()-1; k >= 0; k--) {
            if(callsPerScripid.get(k).getLastCallVersionTwo().equals("buy") &&
                    buy_count<10){
//                record = callsPerScripid.get(k);
                buyPerScripid.add(callsPerScripid.get(k));
                buy_count = buy_count+1;
            }
            if(callsPerScripid.get(k).getLastCallVersionTwo().equals("sell") &&
                    sell_count<10){
//                record = callsPerScripid.get(k);
                sellPerScripid.add(callsPerScripid.get(k));
                sell_count = sell_count+1;
            }
            if(buy_count==10 && sell_count==10){
                break;
            }
        }
        
        System.out.println("Gheuuu");
    }

    public String populateBuyParms() {
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        lastUpdateSelected = targetFormat.format(selectedParm.getLastUpdateTime());
        scripIDSelected = selectedParm.getScripID();
        callTwoSelected = selectedParm.getLastCallVersionTwo();
        System.out.println("Trying to navigate to " + scripID);
        return "ValidateBuyCall";
    }
    
    
}
