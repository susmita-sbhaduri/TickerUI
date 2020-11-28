/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.bucket.DTO;

/**
 *
 * @author bhaduri
 */
public class AwsS3DTO {
    private String uploadFromLocalPath;
    private String uploadLocalFileName;
    private String AWSBucketName;
    private String AWSKeyName;
    private String downloadLocalFileName;
    private String downloadToLocalPath;

    public String getUploadFromLocalPath() {
        return uploadFromLocalPath;
    }

    public void setUploadFromLocalPath(String uploadFromLocalPath) {
        this.uploadFromLocalPath = uploadFromLocalPath;
    }

    public String getUploadLocalFileName() {
        return uploadLocalFileName;
    }

    public void setUploadLocalFileName(String uploadLocalFileName) {
        this.uploadLocalFileName = uploadLocalFileName;
    }

    public String getAWSBucketName() {
        return AWSBucketName;
    }

    public void setAWSBucketName(String AWSBucketName) {
        this.AWSBucketName = AWSBucketName;
    }

    public String getAWSKeyName() {
        return AWSKeyName;
    }

    public void setAWSKeyName(String AWSKeyName) {
        this.AWSKeyName = AWSKeyName;
    }

    public String getDownloadLocalFileName() {
        return downloadLocalFileName;
    }

    public void setDownloadLocalFileName(String downloadLocalFileName) {
        this.downloadLocalFileName = downloadLocalFileName;
    }

    public String getDownloadToLocalPath() {
        return downloadToLocalPath;
    }

    public void setDownloadToLocalPath(String downloadToLocalPath) {
        this.downloadToLocalPath = downloadToLocalPath;
    }
    
}
