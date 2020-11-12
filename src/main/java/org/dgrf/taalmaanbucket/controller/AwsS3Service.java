/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaanbucket.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dgrf.taalmaanbucket.DTO.AwsS3DTO;
import org.dgrf.taalmaanbucket.DTO.AwsS3Response;

/**
 *
 * @author bhaduri
 */
public class AwsS3Service {

    private final AWSCredentials credentials;

    public AwsS3Service( ) {
        credentials = new BasicAWSCredentials ("AKIAJATKGU5B2VBJB2NA","Q/FWwkrozj5L8r/uMK7NRpwOPwMZlElMNkTxy6qX");
    }
    

    public AwsS3Response downloadFromS3(AwsS3DTO awsS3DTO) {
        
        AwsS3Response awsS3Response = new AwsS3Response();
//        if (awsS3Response.getResponseCode() != 0) {
//            return awsS3Response;
//        }
        
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                        .withRegion(Regions.US_WEST_2)
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .build();

        GetObjectRequest request = new GetObjectRequest(awsS3DTO.getAWSBucketName(), awsS3DTO.getAWSKeyName());
        S3Object s3Object = s3.getObject(request);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        try {
            String localFilePath = awsS3DTO.getDownloadToLocalPath() + File.separator + awsS3DTO.getDownloadLocalFileName();
            IOUtils.copy(s3ObjectInputStream, new FileOutputStream(localFilePath));
        } catch (FileNotFoundException ex) {
            awsS3Response.setResponseCode(1);
            awsS3Response.setResponseMessage("File not Found");
            return awsS3Response;
        } catch (IOException ex) {
            awsS3Response.setResponseCode(1);
            awsS3Response.setResponseMessage("IO Error");
            return awsS3Response;
        }
        awsS3Response.setResponseCode(0);
        awsS3Response.setResponseMessage("Success");
        return awsS3Response;
    }

    public AwsS3Response uploadToS3(AwsS3DTO awsS3DTO) {

        
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                        .withRegion(Regions.US_WEST_2)
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .build();
        AwsS3Response awsS3Response = new AwsS3Response();
        //Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        //s3.setRegion(usWest2);
        File fileToUpload = new File(awsS3DTO.getUploadFromLocalPath() + File.separator + awsS3DTO.getUploadLocalFileName());
        try {
            s3.putObject(new PutObjectRequest(awsS3DTO.getAWSBucketName(), awsS3DTO.getAWSKeyName(), fileToUpload));
            
            awsS3Response.setResponseCode(0);
            String fileUrl = s3.getUrl(awsS3DTO.getAWSBucketName(), awsS3DTO.getAWSKeyName()).toString();
            awsS3Response.setResponseMessage(fileUrl);
        } catch (AmazonServiceException ase) {
            String errMsg = "AmazonServiceException Amazon S3 request was rejected with an error response for some reason."
                    + ase.getMessage() + " " + ase.getStatusCode() + " " + ase.getErrorType() + " " + ase.getRequestId();
            awsS3Response.setResponseCode(1);
            awsS3Response.setResponseMessage(errMsg);
        } catch (AmazonClientException ace) {
            String errMsg = "Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.";
            awsS3Response.setResponseCode(1);
            awsS3Response.setResponseMessage(errMsg);
        }
        return awsS3Response;
    }

    public AwsS3Response deleteFromS3(AwsS3DTO awsS3DTO) {

//        AwsS3Response awsS3Response = setCredentials(awsS3DTO);
//        if (awsS3Response.getResponseCode() != 0) {
//            return awsS3Response;
//        }
        AwsS3Response awsS3Response = new AwsS3Response();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                        .withRegion(Regions.US_WEST_2)
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .build();
        try {
            s3.deleteObject(awsS3DTO.getAWSBucketName(), awsS3DTO.getAWSKeyName());
            
            awsS3Response.setResponseCode(0);
            awsS3Response.setResponseMessage("Sucess");
        } catch (SdkClientException e) {
            awsS3Response.setResponseCode(1);
            awsS3Response.setResponseMessage("Delete Failed");
        }

        return awsS3Response;
    }


}
