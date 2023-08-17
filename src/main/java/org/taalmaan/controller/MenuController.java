/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.controller;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author bhaduri
 */
@Named(value = "menuController")
@RequestScoped
public class MenuController {

    private MenuModel menuModel;
    /**
     * Creates a new instance of MenuController
     */
    @Inject
    LoginController loginController;

    public MenuController() {
    }

    @PostConstruct
    void init() {
        if (loginController.getUserID() == null) {
            
        } else {
            menuModel = new DefaultMenuModel();
            
            DefaultMenuItem addSong = new DefaultMenuItem();
            addSong.setTitle("Add Song");
            addSong.setOutcome("AddSongs");
            menuModel.getElements().add(0, addSong);
            
            DefaultMenuItem manageSong = new DefaultMenuItem();
            addSong.setTitle("Manage Song");
            addSong.setOutcome("SonglistUser");
            menuModel.getElements().add(0, manageSong);
            
            

        }
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

}
