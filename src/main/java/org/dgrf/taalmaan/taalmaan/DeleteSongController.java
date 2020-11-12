/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaan.taalmaan;

import java.io.Serializable;
import java.util.List;
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
@Named(value = "deleteSongController")
@ViewScoped
public class DeleteSongController implements Serializable {

    @Inject
    LoginController loginController;
    private List<SongDetailsDTO> deletedSongList;
    private SongDetailsDTO selectedSong;

    /**
     * Creates a new instance of DeleteSongController
     */
    public DeleteSongController() {
    }

    public List<SongDetailsDTO> getDeletedSongList() {
        SongDataService sd = new SongDataService();
        deletedSongList = sd.getAllSongListMarkedDeleted();
        return deletedSongList;
    }

    public void setDeletedSongList(List<SongDetailsDTO> deletedSongList) {
        this.deletedSongList = deletedSongList;
    }

    public SongDetailsDTO getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(SongDetailsDTO selectedSong) {
        this.selectedSong = selectedSong;
    }

    public String undoDelete(SongDetailsDTO songDetailsDTO) {
        SongDataService sds = new SongDataService();
        DBResponse dBResponse = sds.undoSongAsDeleted(songDetailsDTO);
        return "DeletedSongs?faces-redirect=true";
    }

    public String permanentlyDelete(SongDetailsDTO songDetailsDTO) {
        SongDataService sds = new SongDataService();
        DBResponse dBResponse = sds.deleteSong(songDetailsDTO);
        return "DeletedSongs?faces-redirect=true";
    }
}
