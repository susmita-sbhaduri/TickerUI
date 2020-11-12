/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaanservices.TaalMaanServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import org.hedwig.cloud.dto.DataConnDTO;
import org.dgrf.taaldetect.DTO.TaalDetectDTO;
import org.dgrf.taaldetect.DTO.TaalDetectResponse;
import org.dgrf.taaldetect.taalDetect.TaalDetectService;
import org.dgrf.taalmaan.params.DBResponse;
import org.dgrf.taalmaan.params.SONGPARAM;
import org.dgrf.taalmaan.utils.DatabaseConnection;
import org.dgrf.taalmaan.utils.TaalMaanAuthCredtialValue;
import org.dgrf.taalmaan.utils.TaalMaanAuthentication;


import org.dgrf.taalmaanbean.model.SongDetailsDTO;
import org.dgrf.taalmaanbucket.DTO.AwsS3DTO;
import org.dgrf.taalmaanbucket.DTO.AwsS3Response;
import org.dgrf.taalmaanbucket.controller.AwsS3Service;
import org.dgrf.taalmaanservices.DAO.SongDetailsDAO;
import org.dgrf.taalmaanservices.DAO.TaalparamDAO;
import org.dgrf.taalmaanservices.JPA.exceptions.NonexistentEntityException;
import org.dgrf.taalmaanservices.JPA.exceptions.PreexistingEntityException;

import org.dgrf.taalmaanservices.entities.Songdetails;
import org.dgrf.taalmaanservices.entities.Taalparam;

public class SongDataService {

    private EntityManagerFactory emf;

    public SongDataService() {
        getDataBaseConnection();
        emf = DatabaseConnection.EMF;
    }
    
    private void getDataBaseConnection() {
        DataConnDTO dataConnDTO = TaalMaanAuthentication.authenticateSubcription(TaalMaanAuthCredtialValue.AUTH_CREDENTIALS);
        DatabaseConnection dc = new DatabaseConnection(dataConnDTO.getDbAdminUser(), dataConnDTO.getDbAdminPassword(), dataConnDTO.getDbConnUrl());
    }

    public DBResponse insertSong(SongDetailsDTO songDetailsDTO) {
        SongDetailsDAO songDetailsDAO = new SongDetailsDAO(emf);
        Songdetails songvalues = new Songdetails();
        songvalues.setSongid(songDetailsDTO.getId());
        songvalues.setSongname(songDetailsDTO.getSongname());
        songvalues.setGiventaala(songDetailsDTO.getGivenTaala());
        songvalues.setGiventempo(songDetailsDTO.getGivenTempo());
        songvalues.setOtherdetails(songDetailsDTO.getOtherdetails());
        songvalues.setUploadedby(songDetailsDTO.getUploader());
        songvalues.setAlbum(songDetailsDTO.getAlbum());

        songvalues.setDetectedtaala(songDetailsDTO.getDetectedtala());
        songvalues.setDetectedtempo(songDetailsDTO.getDetectedTempo());
        songvalues.setSongpath(songDetailsDTO.getSongPath());
        songvalues.setBucketkey(songDetailsDTO.getBucketKey());
        songvalues.setDeleted(songDetailsDTO.isDeleted());
        songvalues.setProcessed(songDetailsDTO.isProcessed());
        java.util.Date dateTime = new java.util.Date();
        songvalues.setCreateTS(dateTime);
        songvalues.setUpdateTS(dateTime);
        
        try {
            songDetailsDAO.create(songvalues);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(SongDataService.class.getName()).log(Level.SEVERE, null, ex);
            DBResponse dBResponse = new DBResponse(SONGPARAM.DUPLICATE, "Duplicate");
            return dBResponse;
        } catch (Exception ex) {
            Logger.getLogger(SongDataService.class.getName()).log(Level.SEVERE, null, ex);
            DBResponse dBResponse = new DBResponse(SONGPARAM.CONTACT_ADMIN, "Contact Admin");
            return dBResponse;
        }
        DBResponse dBResponse = new DBResponse(SONGPARAM.SUCCESS, "SUCCESS");
        return dBResponse;
    }

    public DBResponse addSong(SongDetailsDTO songDetailsDTO) {
        int newSongId = getMaxSongId();
        String bucketKey = SONGPARAM.AWS_KEY_PREFIX + String.format("%010d", newSongId);
        File tempFile = new File(songDetailsDTO.getSongPath());

        AwsS3DTO awsS3DTO = new AwsS3DTO();
        awsS3DTO.setAWSBucketName(SONGPARAM.AWS_BUCKET);
        awsS3DTO.setUploadFromLocalPath(tempFile.getParent());
        awsS3DTO.setUploadLocalFileName(tempFile.getName());
        awsS3DTO.setAWSKeyName(bucketKey);

        songDetailsDTO.setId(newSongId);
        songDetailsDTO.setBucketKey(bucketKey);
        songDetailsDTO.setDetectedTempo("");
        songDetailsDTO.setDetectedtala("");
        songDetailsDTO.setProcessed(false);
        songDetailsDTO.setDeleted(false);
        DBResponse dBResponse = insertSong(songDetailsDTO);
        if (dBResponse.getResponseCode() != 0) {
            return dBResponse;
        }
        AwsS3Service awsS3Service = new AwsS3Service();
        AwsS3Response awsS3Response = awsS3Service.uploadToS3(awsS3DTO);
        if (awsS3Response.getResponseCode() != 0) {
            dBResponse.setResponseCode(awsS3Response.getResponseCode());
            dBResponse.setResponseMessage(awsS3Response.getResponseMessage());

        } else {
            songDetailsDTO.setSongPath(awsS3Response.getResponseMessage());
            updatePathByAWSBucket(songDetailsDTO);
        }
        return dBResponse;

    }

    public DBResponse detectSongTaal(SongDetailsDTO songDetailsDTO) {

        String songDownloadPath = System.getProperty("user.home");
        File songTempFile = new File(songDownloadPath + File.separator + "tsong.wav");
        if (songTempFile.isFile()) {
            DBResponse dBResponse = new DBResponse(SONGPARAM.DUPLICATE, "Someone else is already working please wait...");
            return dBResponse;
        }
        AwsS3DTO awsS3DTO = new AwsS3DTO();
        awsS3DTO.setAWSBucketName(SONGPARAM.AWS_BUCKET);
        awsS3DTO.setAWSKeyName(songDetailsDTO.getBucketKey());
        awsS3DTO.setDownloadLocalFileName("tsong.wav");
        awsS3DTO.setDownloadToLocalPath(songDownloadPath);
        AwsS3Service awsS3Service = new AwsS3Service();
        AwsS3Response awsS3Response = awsS3Service.downloadFromS3(awsS3DTO);
        if (awsS3Response.getResponseCode() != 0) {
            DBResponse dBResponse = new DBResponse(awsS3Response.getResponseCode(), awsS3Response.getResponseMessage());
            if (songTempFile.isFile()) {
                songTempFile.delete();
            }
            return dBResponse;
        }
        
        Logger.getLogger(SongDataService.class.getName()).log(Level.INFO, "Song downloaded to {0}", songDownloadPath);
        TaalDetectDTO taalDetectDTO = new TaalDetectDTO();
        taalDetectDTO.setInputWavFilePath(songDownloadPath + File.separator + "tsong.wav");
        
        
        Logger.getLogger(SongDataService.class.getName()).log(Level.INFO, "Taal Detection started for {0}{1}tsong.wav", new Object[]{songDownloadPath, File.separator});
        TaalDetectService taalDetectService = new TaalDetectService();
        TaalDetectResponse taalDetectResponse = taalDetectService.detectTaal(taalDetectDTO);
        if (taalDetectResponse.getResponseCode() != 0) {
            DBResponse dBResponse = new DBResponse(taalDetectResponse.getResponseCode(), taalDetectResponse.getResponseMessage());
            songTempFile.delete();
            return dBResponse;
        } else {
            songDetailsDTO.setDetectedtala(taalDetectResponse.getDetectedTaal());
            songDetailsDTO.setDetectedTempo(taalDetectResponse.getDetectedTempo());
            songDetailsDTO.setProcessed(true);
            DBResponse dBResponse = updateSongDetails(songDetailsDTO);
            if (dBResponse.getResponseCode() == 0) {
                dBResponse.setResponseMessage("Detected Taal: " + taalDetectResponse.getDetectedTaal()
                        + "\nDetected Tempo: " + taalDetectResponse.getDetectedTempo());
                songTempFile.delete();
                return dBResponse;
            } else {
                songTempFile.delete();
                return dBResponse;
            }
        }

    }

    public void updatePathByAWSBucket(SongDetailsDTO songDetailsDTO) {
        SongDetailsDAO songDetailsDAO = new SongDetailsDAO(emf);
        Songdetails songDetails = songDetailsDAO.findSongdetails(songDetailsDTO.getId());
        songDetails.setSongpath(songDetailsDTO.getSongPath());
        songDetails.setBucketkey(songDetailsDTO.getBucketKey());
        try {
            songDetailsDAO.edit(songDetails);
        } catch (Exception ex) {
            Logger.getLogger(SongDataService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int[][] getTaalParams (String taal) {
        TaalparamDAO taalparamDAO = new TaalparamDAO(emf);
        List<Taalparam> taalparams = taalparamDAO.findTaalparamEntities();
        List<Taalparam> taalparamSelected = taalparams.stream().filter(t -> t.getTaalparamPK().getTaal().equals(taal)).collect(Collectors.toList());
        int taalParamMatrix[][] = new int[taalparamSelected.size()][2];
        int i = 0;
        for (Taalparam taalParam: taalparamSelected) {
            taalParamMatrix[i][0] = taalParam.getFirststrokecount();
            taalParamMatrix[i][1] = taalParam.getSecondstrokecount();
            i++;
        }
        return taalParamMatrix;
    }
    public DBResponse updateSongDetails(SongDetailsDTO songDetailsDTO) {
        SongDetailsDAO songDetailsDAO = new SongDetailsDAO(emf);
        Songdetails songdetails = songDetailsDAO.findSongdetails(songDetailsDTO.getId());
        if (!songdetails.getAlbum().equals(songDetailsDTO.getAlbum())) {
            songdetails.setAlbum(songDetailsDTO.getAlbum());
        }
        if (!songdetails.getSongname().equals(songDetailsDTO.getSongname())) {
            songdetails.setSongname(songDetailsDTO.getSongname());
        }
        if (!songdetails.getOtherdetails().equals(songDetailsDTO.getOtherdetails())) {
            songdetails.setOtherdetails(songDetailsDTO.getOtherdetails());
        }
        if (!songdetails.getDetectedtaala().equals(songDetailsDTO.getDetectedtala())) {
            songdetails.setDetectedtaala(songDetailsDTO.getDetectedtala());
        }
        if (!songdetails.getDetectedtempo().equals(songDetailsDTO.getDetectedTempo())) {
            songdetails.setDetectedtempo(songDetailsDTO.getDetectedTempo());
        }
        if (!songdetails.getGiventaala().equals(songDetailsDTO.getGivenTaala())) {
            songdetails.setGiventaala(songDetailsDTO.getGivenTaala());
        }
        if (!songdetails.getGiventempo().equals(songDetailsDTO.getGivenTempo())) {
            songdetails.setGiventempo(songDetailsDTO.getGivenTempo());
        }
        if (songdetails.getDeleted() != songDetailsDTO.isDeleted()) {
            songdetails.setDeleted(songDetailsDTO.isDeleted());
        }
        if (songdetails.getProcessed() != songDetailsDTO.isProcessed()) {
            songdetails.setProcessed(songDetailsDTO.isProcessed());
        }
        if (!songdetails.getSongpath().equals(songDetailsDTO.getSongPath())) {
            songdetails.setSongpath(songDetailsDTO.getSongPath());
        }
        if (!songdetails.getBucketkey().equals(songDetailsDTO.getBucketKey())) {
            songdetails.setBucketkey(songDetailsDTO.getBucketKey());
        }
        try {
            songDetailsDAO.edit(songdetails);
            DBResponse dBResponse = new DBResponse(SONGPARAM.SUCCESS, "SUCCESS");
            return dBResponse;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SongDataService.class.getName()).log(Level.SEVERE, null, ex);
            DBResponse dBResponse = new DBResponse(SONGPARAM.NOT_FOUND, "Not Found");
            return dBResponse;
        } catch (Exception ex) {
            Logger.getLogger(SongDataService.class.getName()).log(Level.SEVERE, null, ex);
            DBResponse dBResponse = new DBResponse(SONGPARAM.CONTACT_ADMIN, "DB:Error Contact Admin");
            return dBResponse;
        }

    }

    public DBResponse markSongAsDeleted(SongDetailsDTO songDetailsDTO) {
        songDetailsDTO.setDeleted(true);
        DBResponse dBResponse = updateSongDetails(songDetailsDTO);
        return dBResponse;
    }

    public DBResponse undoSongAsDeleted(SongDetailsDTO songDetailsDTO) {
        songDetailsDTO.setDeleted(false);
        DBResponse dBResponse = updateSongDetails(songDetailsDTO);
        return dBResponse;
    }

    public void markSongAsProcessed(SongDetailsDTO songDetailsDTO) {
        songDetailsDTO.setProcessed(true);
        updateSongDetails(songDetailsDTO);
    }

    public DBResponse deleteSong(SongDetailsDTO songDetailsDTO) {
        AwsS3DTO awsS3DTO = new AwsS3DTO();
        awsS3DTO.setAWSBucketName(SONGPARAM.AWS_BUCKET);
        awsS3DTO.setAWSKeyName(songDetailsDTO.getBucketKey());

        AwsS3Service awsS3Service = new AwsS3Service();
        AwsS3Response awsS3Response = awsS3Service.deleteFromS3(awsS3DTO);
        if (awsS3Response.getResponseCode() != 0) {
            DBResponse dBResponse = new DBResponse(awsS3Response.getResponseCode(), awsS3Response.getResponseMessage());
            return dBResponse;
        }
        SongDetailsDAO songDetailsDAO = new SongDetailsDAO(emf);
        try {
            songDetailsDAO.destroy(songDetailsDTO.getId());
            DBResponse dBResponse = new DBResponse(SONGPARAM.SUCCESS, "Deleted");
            return dBResponse;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SongDataService.class.getName()).log(Level.SEVERE, null, ex);
            DBResponse dBResponse = new DBResponse(SONGPARAM.NOT_FOUND, "Not Found");
            return dBResponse;
        }
    }

    public List<SongDetailsDTO> getSongDetailsList() {

        ArrayList<SongDetailsDTO> songDetailsDTOList = new ArrayList<>();
        SongDetailsDAO songDetailsDAO = new SongDetailsDAO(emf);
        List<Songdetails> songdetailslist = songDetailsDAO.findSongdetailsEntities();
        for (Songdetails songdetails : songdetailslist) {
            SongDetailsDTO songDetailsDTO = new SongDetailsDTO();
            songDetailsDTO.setId(songdetails.getSongid());
            songDetailsDTO.setAlbum(songdetails.getAlbum());
            songDetailsDTO.setOtherdetails(songdetails.getOtherdetails());
            songDetailsDTO.setBucketKey(songdetails.getBucketkey());
            songDetailsDTO.setSongname(songdetails.getSongname());
            songDetailsDTO.setDetectedTempo(songdetails.getDetectedtempo());
            songDetailsDTO.setDetectedtala(songdetails.getDetectedtaala());
            songDetailsDTO.setGivenTaala(songdetails.getGiventaala());
            songDetailsDTO.setGivenTempo(songdetails.getGiventempo());
            songDetailsDTO.setSongPath(songdetails.getSongpath());
            songDetailsDTO.setHtmlAudioPath(getHtmlAudioTag(songdetails.getSongpath()));
            songDetailsDTO.setDeleted(songdetails.getDeleted());
            songDetailsDTO.setProcessed(songdetails.getProcessed());
            songDetailsDTO.setUploader(songdetails.getUploadedby());
            songDetailsDTOList.add(songDetailsDTO);
        }
        return songDetailsDTOList;
    }

    public List<SongDetailsDTO> getActiveProcessedSongListByUser(String user) {
        List<SongDetailsDTO> songDetailsDTOList = getSongDetailsList();
        ArrayList<SongDetailsDTO> songDetailsDTOListByUser = new ArrayList<>();
        for (SongDetailsDTO songDetailsDTO : songDetailsDTOList) {
            if (songDetailsDTO.getUploader().equals(user)) {
                if (songDetailsDTO.isProcessed()) {
                    if (!songDetailsDTO.isDeleted()) {
                        songDetailsDTOListByUser.add(songDetailsDTO);
                    }
                }

            }
        }
        return songDetailsDTOListByUser;
    }

    public List<SongDetailsDTO> getActiveNotProcessedSongListByUser(String user) {
        List<SongDetailsDTO> songDetailsDTOList = getSongDetailsList();
        ArrayList<SongDetailsDTO> songDetailsDTOListByUser = new ArrayList<>();
        for (SongDetailsDTO songDetailsDTO : songDetailsDTOList) {
            if (songDetailsDTO.getUploader().equals(user)) {
                if (!songDetailsDTO.isProcessed()) {
                    if (!songDetailsDTO.isDeleted()) {
                        songDetailsDTOListByUser.add(songDetailsDTO);
                    }
                }

            }
        }
        return songDetailsDTOListByUser;
    }

    public List<SongDetailsDTO> getAllSongListMarkedDeleted() {
        List<SongDetailsDTO> songDetailsDTOList = getSongDetailsList();
        ArrayList<SongDetailsDTO> songDetailsDTOListMarkedDeleted = new ArrayList<>();
        for (SongDetailsDTO songDetailsDTO : songDetailsDTOList) {
            if (songDetailsDTO.isDeleted()) {
                songDetailsDTOListMarkedDeleted.add(songDetailsDTO);
            }
        }
        return songDetailsDTOListMarkedDeleted;
    }

    public List<SongDetailsDTO> getAllSongListActive() {
        List<SongDetailsDTO> songDetailsDTOList = getSongDetailsList();
        ArrayList<SongDetailsDTO> songDetailsDTOListActive = new ArrayList<>();
        for (SongDetailsDTO songDetailsDTO : songDetailsDTOList) {
            if (!songDetailsDTO.isDeleted()) {
                if (songDetailsDTO.isProcessed()) {
                    songDetailsDTOListActive.add(songDetailsDTO);
                }
            }
        }
        return songDetailsDTOListActive;
    }

    private int getMaxSongId() {

        int maxid;
        SongDetailsDAO songDetailsDAO = new SongDetailsDAO(emf);
        maxid = songDetailsDAO.getMaxSongId();
        int songid = maxid + 1;
        return songid;
    }

    private String getHtmlAudioTag(String httpPath) {
        String actualPath = "<audio controls> \n"
                + "	<source src=\"" + httpPath + "\" type=\"audio/wav\">\n"
                + "	Your browser does not support the audio element. \n"
                + "	</audio>";
        return actualPath;
    }

}
