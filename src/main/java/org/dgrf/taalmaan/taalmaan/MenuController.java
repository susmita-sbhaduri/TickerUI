/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaan.taalmaan;

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
            
            DefaultMenuItem addSong = new DefaultMenuItem("Add Song");
            addSong.setOutcome("AddSongs");
            menuModel.addElement(addSong);
            DefaultMenuItem manageSong = new DefaultMenuItem("Manage Songs");
            manageSong.setOutcome("SonglistUser");
            menuModel.addElement(manageSong);
            DefaultMenuItem processSong = new DefaultMenuItem("Detect Tālā");
            processSong.setOutcome("ProcessSong");
            menuModel.addElement(processSong);
            if (loginController.getUserAuthDTO().getRoleId() == 1) {
                DefaultMenuItem deletedSongs = new DefaultMenuItem("Deleted Songs");
                deletedSongs.setOutcome("DeletedSongs");
                menuModel.addElement(deletedSongs);
            }
        }
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

}
