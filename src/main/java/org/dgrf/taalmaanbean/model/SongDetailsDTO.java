/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaanbean.model;

import java.io.Serializable;

/**
 *
 * @author dgrf-iv
 */
public class SongDetailsDTO implements Serializable{
    
    private int id;
    private String songname;
    private String givenTaala;
    private String givenTempo;
    private String album;
    private String otherdetails;
    private String songPath;
    private String detectedtala;
    private String detectedTempo;
    private String uploader;
    private boolean deleted;
    private boolean processed;
   
    private String htmlAudioPath;
    private String bucketKey;

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getGivenTaala() {
        return givenTaala;
    }

    public void setGivenTaala(String givenTaala) {
        this.givenTaala = givenTaala;
    }

    public String getGivenTempo() {
        return givenTempo;
    }

    public void setGivenTempo(String givenTempo) {
        this.givenTempo = givenTempo;
    }



    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getOtherdetails() {
        return otherdetails;
    }

    public void setOtherdetails(String otherdetails) {
        this.otherdetails = otherdetails;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }



    public String getDetectedtala() {
        return detectedtala;
    }

    public void setDetectedtala(String detectedtala) {
        this.detectedtala = detectedtala;
    }

    public String getDetectedTempo() {
        return detectedTempo;
    }

    public void setDetectedTempo(String detectedTempo) {
        this.detectedTempo = detectedTempo;
    }

 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }



    public String getHtmlAudioPath() {
        return htmlAudioPath;
    }

    public void setHtmlAudioPath(String htmlAudioPath) {
        this.htmlAudioPath = htmlAudioPath;
    }

    public String getBucketKey() {
        return bucketKey;
    }

    public void setBucketKey(String bucketKey) {
        this.bucketKey = bucketKey;
    }
    
}
