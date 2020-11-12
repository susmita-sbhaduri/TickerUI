/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaan.taalmaan;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.dgrf.taalmaan.params.DBResponse;
import org.dgrf.taalmaanbean.model.SongDetailsDTO;
import org.dgrf.taalmaanservices.TaalMaanServices.SongDataService;

/**
 *
 * @author bhaduri
 */
@Named(value = "userSongMaintenanceController")
@ViewScoped
public class UserSongMaintenanceController implements Serializable {

    @Inject
    LoginController loginController;
    private List<SongDetailsDTO> userProcessedSongList;
    private SongDetailsDTO selectedSong;

    /**
     * Creates a new instance of UserSongMaintenanceController
     */
    public UserSongMaintenanceController() {
    }

    public List<SongDetailsDTO> getUserProcessedSongList() {

        String userId = loginController.getUserAuthDTO().getUserId();
        SongDataService sd = new SongDataService();
        userProcessedSongList = sd.getActiveProcessedSongListByUser(userId);
        return userProcessedSongList;
    }

    public void setUserProcessedSongList(List<SongDetailsDTO> userProcessedSongList) {
        this.userProcessedSongList = userProcessedSongList;
    }

    public SongDetailsDTO getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(SongDetailsDTO selectedSong) {
        this.selectedSong = selectedSong;
    }

    public String markAsDelete(SongDetailsDTO songDetailsDTO) {
        SongDataService sds = new SongDataService();
        DBResponse dBResponse = sds.markSongAsDeleted(songDetailsDTO);
        return "SonglistUser?faces-redirect=true";
    }

    public String updateSong() {
        SongDataService sds = new SongDataService();
        FacesMessage message;

        DBResponse dBResponse = sds.updateSongDetails(selectedSong);
        if (dBResponse.getResponseCode() != 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "DB Error", dBResponse.getResponseMessage());
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success.", dBResponse.getResponseMessage());
        }
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        f.addMessage(null, message);
        return "SonglistUser?faces-redirect=true";
    }
}
