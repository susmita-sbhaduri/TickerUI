/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.bhaduri.datatransfer.DTO.ScripID;
import org.bhaduri.minutedataaccess.services.MasterDataServices;

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
    public String showPreviousCalls() {
        
        scripID = selectedScripID.getScripID();
        System.out.println("Trying to navigate to " + scripID);
        return "ScripChart";
    }

}
