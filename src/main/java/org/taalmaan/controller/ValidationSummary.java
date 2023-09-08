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
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.bhaduri.datatransfer.DTO.RecordCallPrice;
import org.bhaduri.datatransfer.DTO.RecordMinute;
import org.bhaduri.minutedataaccess.services.MasterDataServices;

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
    private String priceSecond;
    private String callListLen;
    private List<RecordMinute> minuteDataValid;
        
    @PostConstruct
    public void init() {
//        listMinuteDetails();
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

    public String getCallListLen() {
        return callListLen;
    }

    public void setCallListLen(String callListLen) {
        this.callListLen = callListLen;
    }

    public String getPriceSecond() {
        return priceSecond;
    }

    public void setPriceSecond(String priceSecond) {
        this.priceSecond = priceSecond;
    }

    public List<RecordMinute> getMinuteDataValid() {
        return minuteDataValid;
    }

    public void setMinuteDataValid(List<RecordMinute> minuteDataValid) {
        this.minuteDataValid = minuteDataValid;
    }
      
    
    public void listMinuteDetails() {
        if (callTwoFirst.equals("buy")) {
            if (callListLen.equals("0") == false) {
                MasterDataServices masterDataService = new MasterDataServices();
                List<RecordMinute> minuteDataForRange = new ArrayList<>();
                minuteDataValid = new ArrayList<>();
                DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date firstDate = null;
                Date secondDate = null;
                try {
                    firstDate = targetFormat.parse(lastUpdateFirst);
                    secondDate = targetFormat.parse(lastUpdateSecond);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                RecordMinute record = new RecordMinute();
                minuteDataForRange = masterDataService.getMindataForRange(scripId,
                        secondDate, firstDate);
                Double sellPrice = Double.parseDouble(priceSecond);
                for (int k = 0; k < minuteDataForRange.size(); k++) {
                    if (minuteDataForRange.get(k).getDaylastprice() < sellPrice) {
                        record.setScripID(minuteDataForRange.get(k).getScripID());
                        record.setLastUpdateTime(minuteDataForRange.get(k).getLastUpdateTime());
                        record.setOpenprice(minuteDataForRange.get(k).getOpenprice());
                        record.setDaylastprice(minuteDataForRange.get(k).getDaylastprice());
                        record.setDayhighprice(minuteDataForRange.get(k).getDayhighprice());
                        record.setDaylowprice(minuteDataForRange.get(k).getDaylowprice());
                        record.setPrevcloseprice(minuteDataForRange.get(k).getPrevcloseprice());
                        record.setTotaltradedvolume(minuteDataForRange.get(k).getTotaltradedvolume());
                        minuteDataValid.add(record);
                        record = new RecordMinute();
                    }
                }
                if(minuteDataValid.isEmpty()){
                    System.out.println("Buy price is not satisfied w.r.t selected Sell call for this scripid=" + scripId);
                }
            }
        }
        
        if (callTwoFirst.equals("sell")) {
            if (callListLen.equals("0") == false) {
                MasterDataServices masterDataService = new MasterDataServices();
                List<RecordMinute> minuteDataForRange = new ArrayList<>();
                minuteDataValid = new ArrayList<>();
                DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date firstDate = null;
                Date secondDate = null;
                try {
                    firstDate = targetFormat.parse(lastUpdateFirst);
                    secondDate = targetFormat.parse(lastUpdateSecond);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                RecordMinute record = new RecordMinute();
                minuteDataForRange = masterDataService.getMindataForRange(scripId,
                        secondDate, firstDate);
                Double buyPrice = Double.parseDouble(priceSecond);
                for (int k = 0; k < minuteDataForRange.size(); k++) {
                    if (minuteDataForRange.get(k).getDaylastprice() > buyPrice) {
                        record.setScripID(minuteDataForRange.get(k).getScripID());
                        record.setLastUpdateTime(minuteDataForRange.get(k).getLastUpdateTime());
                        record.setOpenprice(minuteDataForRange.get(k).getOpenprice());
                        record.setDaylastprice(minuteDataForRange.get(k).getDaylastprice());
                        record.setDayhighprice(minuteDataForRange.get(k).getDayhighprice());
                        record.setDaylowprice(minuteDataForRange.get(k).getDaylowprice());
                        record.setPrevcloseprice(minuteDataForRange.get(k).getPrevcloseprice());
                        record.setTotaltradedvolume(minuteDataForRange.get(k).getTotaltradedvolume());
                        minuteDataValid.add(record);
                        record = new RecordMinute();
                    }
                }
                if(minuteDataValid.isEmpty()){
                    System.out.println("Sell price is not satisfied w.r.t selected buy call for this scripid=" + scripId);
                }
            }
        }
    }
        
//        DateFormat originalFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
        
//       
//            try {
//                Date dateselected = originalFormat.parse(lastUpdateSelected);
//                reverseCallList = masterDataService.listReverseCalls(scripIDSelected,
//                        dateselected, "sell");
//            } catch (ParseException ex) {
//                ex.printStackTrace();
//            }
      
//    public String populateAllParms() {
//        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        lastUpdateSecond = targetFormat.format(selectedAllParm.getLastUpdateTime());
//        
//        scripIDSecond = selectedAllParm.getScripID();
//        callTwoSecond = selectedAllParm.getLastCallVersionTwo();
////        System.out.println("Trying to navigate to " + scripID);
//        return "ValidationSummary";
//    }

}
