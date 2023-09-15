/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.bhaduri.datatransfer.DTO.DataStoreNames;
import org.bhaduri.datatransfer.DTO.ScripID;

/**
 *
 * @author sb
 */
@Named(value = "listScripId")
@ViewScoped
@ManagedBean
public class ListScripId implements Serializable {

    /**
     * Creates a new instance of GenerateLastCall
     */
    private List<ScripID> scripIDList;
    private ScripID selectedScripID;
    private String scripID;

    @PostConstruct
    public void init() {
        generateScriptId();
    }

    public List<ScripID> getScripIDList() {
        return scripIDList;
    }

    public void setScripIDList(List<ScripID> scripIDList) {
        this.scripIDList = scripIDList;
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
    
    public void generateScriptId() {
        File directory = new File(DataStoreNames.TICKER_DATA_DETAILS);
        List listFileArray = Arrays.asList(directory.list());
        Collections.sort(listFileArray); //directories are sorted as per their name
        int dirCount = listFileArray.size();
        scripIDList = new ArrayList<>();

        for (int k = 0; k < dirCount; k++) {
            ScripID scripID = new ScripID();
            scripID.setScripID(listFileArray.get(k).toString());
            scripIDList.add(scripID);
        }
    }
    public String showSelectedScrip() {
        
        scripID = selectedScripID.getScripID();
        System.out.println("Trying to navigate to " + scripID);
        return "GeneratedCall";
    }
}
