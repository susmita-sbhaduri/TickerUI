package org.taalmaan.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.taalmaan.params.DBResponse;
import org.taalmaan.bean.model.SongDetailsDTO;
import org.taalmaan.services.TaalMaanServices.SongDataService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author bhaduri
 */
@Named(value = "addSongController")
@ViewScoped
public class AddSongController implements Serializable {

    SongDetailsDTO songDetailsDTO;
    private boolean songUploaded;
    /**
     * Creates a new instance of AddSongController
     */
    @Inject
    LoginController loginController;

    public AddSongController() {
        songDetailsDTO = new SongDetailsDTO();
        songUploaded = false;
    }

    public SongDetailsDTO getSongDetailsDTO() {
        return songDetailsDTO;
    }

    public void setSongDetailsDTO(SongDetailsDTO songDetailsDTO) {
        this.songDetailsDTO = songDetailsDTO;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public String addSong() {


        
        if (!isSongUploaded()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Song file not selected");
            FacesContext.getCurrentInstance().addMessage(null, message);
             return "AddSongs";
        } else {

            songDetailsDTO.setUploader(loginController.getUserAuthDTO().getUserId());

            SongDataService sd = new SongDataService();
            DBResponse dBResponse = sd.addSong(songDetailsDTO);
            if (dBResponse.getResponseCode() == 0) {
                return "AddSuccess";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,"Error", dBResponse.getResponseMessage());
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "AddSongs";
            }
        }
       
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedSong = event.getFile();

        byte[] bytes = null;

        if (null != uploadedSong) {
            bytes = uploadedSong.getContents();
            String tempPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            
            FileOutputStream fo;

            try {
                fo = new FileOutputStream(new File(tempPath + "temp.wav"));
                try (BufferedOutputStream stream = new BufferedOutputStream(fo)) {
                    stream.write(bytes);
                    stream.close();
                    songDetailsDTO.setSongPath(tempPath + "temp.wav");

                    songUploaded = true;

                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(AddSongController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AddSongController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public boolean isSongUploaded() {
        return songUploaded;
    }

    public void setSongUploaded(boolean songUploaded) {
        this.songUploaded = songUploaded;
    }

}
