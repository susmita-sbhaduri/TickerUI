/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.db.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bhaduri
 */
@Entity
@Table(name = "songdetails")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Songdetails.findAll", query = "SELECT s FROM Songdetails s")})
public class Songdetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "songid")
    private Integer songid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "songname")
    private String songname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "giventaala")
    private String giventaala;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "detectedtaala")
    private String detectedtaala;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "detectedtempo")
    private String detectedtempo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "songpath")
    private String songpath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "bucketkey")
    private String bucketkey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "uploadedby")
    private String uploadedby;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "processed")
    private boolean processed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "createTS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updateTS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTS;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "giventempo")
    private String giventempo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "album")
    private String album;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "otherdetails")
    private String otherdetails;

    public Songdetails() {
    }

    public Songdetails(Integer songid) {
        this.songid = songid;
    }

    public Songdetails(Integer songid, String songname, String giventaala, String detectedtaala, String detectedtempo, String songpath, String bucketkey, String uploadedby, boolean deleted, boolean processed, Date createTS, Date updateTS, String giventempo, String album, String otherdetails) {
        this.songid = songid;
        this.songname = songname;
        this.giventaala = giventaala;
        this.detectedtaala = detectedtaala;
        this.detectedtempo = detectedtempo;
        this.songpath = songpath;
        this.bucketkey = bucketkey;
        this.uploadedby = uploadedby;
        this.deleted = deleted;
        this.processed = processed;
        this.createTS = createTS;
        this.updateTS = updateTS;
        this.giventempo = giventempo;
        this.album = album;
        this.otherdetails = otherdetails;
    }

    public Integer getSongid() {
        return songid;
    }

    public void setSongid(Integer songid) {
        this.songid = songid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getGiventaala() {
        return giventaala;
    }

    public void setGiventaala(String giventaala) {
        this.giventaala = giventaala;
    }

    public String getDetectedtaala() {
        return detectedtaala;
    }

    public void setDetectedtaala(String detectedtaala) {
        this.detectedtaala = detectedtaala;
    }

    public String getDetectedtempo() {
        return detectedtempo;
    }

    public void setDetectedtempo(String detectedtempo) {
        this.detectedtempo = detectedtempo;
    }

    public String getSongpath() {
        return songpath;
    }

    public void setSongpath(String songpath) {
        this.songpath = songpath;
    }

    public String getBucketkey() {
        return bucketkey;
    }

    public void setBucketkey(String bucketkey) {
        this.bucketkey = bucketkey;
    }

    public String getUploadedby() {
        return uploadedby;
    }

    public void setUploadedby(String uploadedby) {
        this.uploadedby = uploadedby;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Date getCreateTS() {
        return createTS;
    }

    public void setCreateTS(Date createTS) {
        this.createTS = createTS;
    }

    public Date getUpdateTS() {
        return updateTS;
    }

    public void setUpdateTS(Date updateTS) {
        this.updateTS = updateTS;
    }

    public String getGiventempo() {
        return giventempo;
    }

    public void setGiventempo(String giventempo) {
        this.giventempo = giventempo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (songid != null ? songid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Songdetails)) {
            return false;
        }
        Songdetails other = (Songdetails) object;
        if ((this.songid == null && other.songid != null) || (this.songid != null && !this.songid.equals(other.songid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.taalmaan.db.entities.Songdetails[ songid=" + songid + " ]";
    }
    
}
