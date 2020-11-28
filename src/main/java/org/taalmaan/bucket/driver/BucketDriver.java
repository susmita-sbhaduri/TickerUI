/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.bucket.driver;

import org.taalmaan.bucket.DTO.AwsS3DTO;
import org.taalmaan.bucket.DTO.AwsS3Response;
import org.taalmaan.bucket.controller.AwsS3Service;

/**
 *
 * @author dgrf-vi
 */
public class BucketDriver {

    public static void main(String args[]) {
        download();
        
    }

    public static void download() {
        AwsS3DTO awsS3DTO = new AwsS3DTO();
        awsS3DTO.setAWSBucketName("taalmaan-bucket");
        awsS3DTO.setAWSKeyName("uploaded-songs/120171208");
        awsS3DTO.setDownloadLocalFileName("paglasong2.wav");
        //awsS3DTO.setDownloadToLocalPath("/home/bhaduri");
        awsS3DTO.setDownloadToLocalPath(System.getProperty("user.dir"));
        AwsS3Service awsS3Service = new AwsS3Service();
        awsS3Service.downloadFromS3(awsS3DTO);
        
    }
    public static void upload() {
        AwsS3DTO awsS3DTO = new AwsS3DTO();
        String key = "uploaded-songs/"+String.format("%010d",1);
        
        awsS3DTO.setAWSBucketName("taalmaan-bucket");
        awsS3DTO.setAWSKeyName(key);
        awsS3DTO.setUploadLocalFileName("paglasong.wav");
        awsS3DTO.setUploadFromLocalPath("/home/bhaduri");
        AwsS3Service awsS3Service = new AwsS3Service();
        AwsS3Response awsS3Response = awsS3Service.uploadToS3(awsS3DTO);
        System.out.println(awsS3Response.getResponseMessage());
        System.out.println("Done");        
    }
    public static void delete() {
        AwsS3DTO awsS3DTO = new AwsS3DTO();
        String key = "uploaded-songs/"+String.format("%010d",1);
        
        awsS3DTO.setAWSBucketName("taalmaan-bucket");
        awsS3DTO.setAWSKeyName(key);
        //awsS3DTO.setUploadLocalFileName("paglasong.wav");
        //awsS3DTO.setUploadFromLocalPath("/home/dgrf-vi");
        AwsS3Service awsS3Service = new AwsS3Service();
        AwsS3Response awsS3Response = awsS3Service.deleteFromS3(awsS3DTO);
        System.out.println(awsS3Response.getResponseMessage());
        System.out.println("Done");        
    }    
}
