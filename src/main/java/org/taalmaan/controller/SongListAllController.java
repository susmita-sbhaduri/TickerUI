/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.hedwig.cloud.client.DataConnClient;
import org.hedwig.cloud.dto.DataConnDTO;
import org.taalmaan.utils.DatabaseConnection;
import org.taalmaan.utils.TaalMaanAuthCredtialValue;
import org.taalmaan.utils.UserCredential;
import org.taalmaan.bean.model.SongDetailsDTO;
import org.taalmaan.services.TaalMaanServices.SongDataService;

/**
 *
 * @author bhaduri
 */
@Named(value = "songListAllController")
@ViewScoped
public class SongListAllController implements Serializable {

    private List<SongDetailsDTO> processedSongList;
    private SongDetailsDTO selectedSong;
    @Inject
    LoginController loginController;

    /**
     * Creates a new instance of SongListAllController
     */
    public SongListAllController() {

    }
    
    public void fetchProcessedSongList() {

        boolean authProduct = authProduct();
        if (authProduct) {
            SongDataService sd = new SongDataService();
            processedSongList = sd.getAllSongListActive();
        } else {
            processedSongList = new ArrayList<>();
        }

    }

    public List<SongDetailsDTO> getProcessedSongList() {
        return processedSongList;
    }

    public void setProcessedSongList(List<SongDetailsDTO> processedSongList) {
        this.processedSongList = processedSongList;
    }

    public SongDetailsDTO getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(SongDetailsDTO selectedSong) {
        this.selectedSong = selectedSong;
    }

    private boolean authProduct() {
        
        DataConnDTO dataConnDTO = new DataConnDTO();
        dataConnDTO.setCloudAuthCredentials(TaalMaanAuthCredtialValue.AUTH_CREDENTIALS);
        DataConnClient dataConnClient = new DataConnClient();
        dataConnDTO = dataConnClient.getDataConnParams(dataConnDTO);

        FacesMessage message;
        
        if (dataConnDTO == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Data Access Failure", "Subscription not active.");
            FacesContext f = FacesContext.getCurrentInstance();
            f.addMessage(null, message);
            return false;
        }
        
        DatabaseConnection dc = new DatabaseConnection(dataConnDTO.getDbAdminUser(), dataConnDTO.getDbAdminPassword(), dataConnDTO.getDbConnUrl());

        return true;
    }
    

}
