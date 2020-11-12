/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taaldetect.DTO;

/**
 *
 * @author bhaduri
 */
public class TaalDetectResponse {
    private int ResponseCode;
    private String ResponseMessage;
    private String detectedTaal;
    private String detectedTempo;

    public TaalDetectResponse(int ResponseCode, String ResponseMessage) {
        this.ResponseCode = ResponseCode;
        this.ResponseMessage = ResponseMessage;
    }

    public int getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(int ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }



    public String getDetectedTaal() {
        return detectedTaal;
    }

    public void setDetectedTaal(String detectedTaal) {
        this.detectedTaal = detectedTaal;
    }

    public String getDetectedTempo() {
        return detectedTempo;
    }

    public void setDetectedTempo(String detectedTempo) {
        this.detectedTempo = detectedTempo;
    }
    
    
}
