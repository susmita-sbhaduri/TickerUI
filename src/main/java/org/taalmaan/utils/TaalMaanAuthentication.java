/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.utils;


import org.hedwig.cloud.dto.DataConnDTO;
import org.hedwig.cloud.dto.HedwigAuthCredentials;
import org.hedwig.cloud.client.DataConnClient;

/**
 *
 * @author bhaduri
 */
public class TaalMaanAuthentication {

    public static DataConnDTO authenticateSubcription(HedwigAuthCredentials authCredentials) {
        int productId = authCredentials.getProductId();
        int tenantId = authCredentials.getTenantId();
        DataConnClient dataConnClient = new DataConnClient();
        DataConnDTO dataConnDTO = new DataConnDTO();
        dataConnDTO.setCloudAuthCredentials(authCredentials);
        dataConnDTO = dataConnClient.getDataConnParams(dataConnDTO);
        return dataConnDTO;
    }
    
//    public static void setCMSAuthCredentials( int productId, int tenantId, String userId, String password ){
//        CMSClientAuthDetails.USER_ID = userId;
//        CMSClientAuthDetails.PASSWORD = password;
//        CMSClientAuthDetails.PRODUCT_ID = productId;
//        CMSClientAuthDetails.TENANT_ID = tenantId;
//    }
}
