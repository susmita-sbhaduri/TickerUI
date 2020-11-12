/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taaldetect.taalDetect;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dgrf.taaldetect.DTO.TaalDetectDTO;
import org.dgrf.taaldetect.DTO.TaalDetectResponse;

/**
 *
 * @author bhaduri
 */
public class TaalDetectService {

    public TaalDetectResponse detectTaal(TaalDetectDTO taalDetectDTO) {
        String inputWavFilePath = taalDetectDTO.getInputWavFilePath();
        TaalDetect taal = new TaalDetect(inputWavFilePath);
        try {
            taal.calcTaalaTempo();
            String detectedTaal = taal.getTaala();
            double detectedTempoDouble = Math.floor(taal.getTempo());
            String detectedTempo = String.valueOf(detectedTempoDouble);
            TaalDetectResponse taalDetectResponse = new TaalDetectResponse(0, "SUCESS");
            taalDetectResponse.setDetectedTaal(detectedTaal);
            taalDetectResponse.setDetectedTempo(detectedTempo);
            return taalDetectResponse;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TaalDetectService.class.getName()).log(Level.SEVERE, null, ex);
            TaalDetectResponse taalDetectResponse = new TaalDetectResponse(1, "File has wrong encoding.");
            return taalDetectResponse;
        }

    }

}
