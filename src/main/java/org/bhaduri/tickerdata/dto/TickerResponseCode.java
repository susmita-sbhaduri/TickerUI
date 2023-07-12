/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhaduri.tickerdata.dto;

/**
 *
 * @author bhaduri
 */
public class TickerResponseCode {
    //generic
    public static int SUCCESS = 0;
    public static int SERVICE_CONNECTION_FAILURE = 001;
    public static int JSON_FORMAT_PROBLEM = 002;
    //cms response codes
    public static int TERM_NOT_EXISTS = 100;
    public static int TERM_META_NOT_EXISTS = 101;
    public static int TERM_INSTANCE_NOT_EXISTS = 102;
    //databases responses
    public static int DB_DUPLICATE =200;
    public static int DB_NON_EXISTING = 201;
    public static int DB_ILLEGAL_ORPHAN = 203;
    public static int DB_SEVERE = 204;
    public static int DB_ROLLBACK_ERROR = 205;
    //authentication responses
    
    public static int SUBSCRIPTION_NOT_ACTIVE = 301;
    public static int USER_EXCEEDED = 302;
    public static int NO_RESPONSE = 303;
    public static int NO_USER = 304;
    public static int USER_INACTIVE = 305;
    public static int PASSWORD_INCORRECT = 306;
    public static int NO_SUBCRIPTION = 307;
    public static int SUBSCRITION_AUTH_FAIL = 308;
    public static int DATA_CONN_ERROR = 309;
    public static int DB_SIZE_EXCEED = 310;
    public static int WRONG_ATTEMPTS_EXCEED = 311;
    
        //Amazon S3 Bucket responses
    
    public static int AmazonServiceException = 501;
    public static int AmazonClientException = 502;
    
    
}
