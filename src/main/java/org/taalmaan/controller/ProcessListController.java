/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.taalmaan.params.DBResponse;
import org.taalmaan.bean.model.SongDetailsDTO;
import org.taalmaan.services.TaalMaanServices.SongDataService;

/**
 *
 * @author bhaduri
 */
@Named(value = "processListController")
@ViewScoped
public class ProcessListController implements Serializable {

    @Inject
    LoginController loginController;
    private List<SongDetailsDTO> notProcessedSongList;
    private SongDetailsDTO selectedSong;
    private String detectedTaalMessage;

    /**
     * Creates a new instance of ProcessListController
     */
    public ProcessListController() {

    }

    public List<SongDetailsDTO> getNotProcessedSongList() {

        String userId = loginController.getUserAuthDTO().getUserId();
        SongDataService sd = new SongDataService();
        notProcessedSongList = sd.getActiveNotProcessedSongListByUser(userId);
        return notProcessedSongList;
    }

    public void setNotProcessedSongList(List<SongDetailsDTO> notProcessedSongList) {
        this.notProcessedSongList = notProcessedSongList;
    }

    public SongDetailsDTO getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(SongDetailsDTO selectedSong) {
        this.selectedSong = selectedSong;
    }

    public void detectTaala(SongDetailsDTO detectSong) {
        
        SongDataService sd = new SongDataService();
        DBResponse dBResponse = sd.detectSongTaal(detectSong);
        if (dBResponse.getResponseCode() != 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", dBResponse.getResponseMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
            detectedTaalMessage = dBResponse.getResponseMessage();
        } else {
            detectedTaalMessage = "Taala Detection complete for "+detectSong.getSongname()+" "+ dBResponse.getResponseMessage();
        }
        //detectedTaalMessage = "Pok POk";
        
    }

    public String getDetectedTaalMessage() {
        return detectedTaalMessage;
    }

    public void setDetectedTaalMessage(String detectedTaalMessage) {
        this.detectedTaalMessage = detectedTaalMessage;
    }
    

}
