/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.controller;

import java.io.Serializable;
import java.util.List;
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
