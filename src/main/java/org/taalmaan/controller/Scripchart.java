/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    
    public Scripchart() {
    }
    public void loadScripCalls() {
        buyPerScripid = new ArrayList<>();
        sellPerScripid = new ArrayList<>();
        List<RecordCallPrice> callsPerScripid;
        int buy_count =0;
        int sell_count =0;
        
        MasterDataServices masterDataService = new MasterDataServices();
        callsPerScripid = masterDataService.callListPerScrip(scripID);
        for (int k = callsPerScripid.size()-1; k > callsPerScripid.size(); k--) {
            RecordCallPrice record = new RecordCallPrice();
            if(callsPerScripid.get(k).getLastCallVersionTwo().equals("buy")){
//                record = callsPerScripid.get(k);
                buyPerScripid.add(callsPerScripid.get(k));
                buy_count = buy_count+1;
            }
            if(callsPerScripid.get(k).getLastCallVersionTwo().equals("sell")){
//                record = callsPerScripid.get(k);
                sellPerScripid.add(callsPerScripid.get(k));
                sell_count = buy_count+1;
            }
            if(buy_count==10 && sell_count==10){
                break;
            }
        }
        
        System.out.println("Gheuuu");
    }

    
    
    
}
