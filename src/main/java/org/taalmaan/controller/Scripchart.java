/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.taalmaan.controller;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author sb
 */
@Named(value = "scripchart")
@ViewScoped
public class Scripchart implements Serializable {
    private String scripID;
    /**
     * Creates a new instance of Scripchart
     */
    public Scripchart() {
    }
    public void loadScripCalls() {
        System.out.println("Gheuuu");
    }

    public String getScripID() {
        return scripID;
    }

    public void setScripID(String scripID) {
        this.scripID = scripID;
    }
    
    
}
