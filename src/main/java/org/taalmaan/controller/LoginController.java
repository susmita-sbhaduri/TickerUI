/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.bhaduri.tickerdata.dto.TickerResponseCode;
import org.bhaduri.tickerdata.dto.UserAuthDTO;

/**
 *
 * @author dgrf-iv
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

//    private int tenantID;
//    private String tenantName;
//    private int productID;
    private String userID;
    private String password;
    private UserAuthDTO userAuthDTO;
//    private List<SelectItem> tenantMap;
    @Inject
    private ServletContext context;
    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {
    }

    @PostConstruct
    private void init() {
        userAuthDTO = new UserAuthDTO();
    }

    public void fillLOginFormValues() {
//        String hedwigServer = context.getInitParameter("HedwigServerName");
//        String hedwigServerPort = context.getInitParameter("HedwigServerPort");
//        TenantListClient dgrftlc = new TenantListClient(hedwigServer,hedwigServerPort);
//        List<TenantDTO> tenantDTOs = dgrftlc.getTenantList(productID);
//        tenantMap = tenantDTOs.stream().map(tenant -> {
//            SelectItem selectItem = new SelectItem(tenant.getTenantId(), tenant.getName());
//            return selectItem;
//        }).collect(Collectors.toList());

    }
    
    

    public String login() {
        
        

        userAuthDTO.setUserId(userID);
        userAuthDTO.setPassword(password);
        
        userAuthDTO = authenticateUser(userAuthDTO);
        FacesMessage message;
        
        if (userAuthDTO.getResponseCode() == TickerResponseCode.SUCCESS) {
//            setAuthCredentials();
            return "SonglistUser?faces-redirect=true";
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Wrong Password");
            FacesContext f = FacesContext.getCurrentInstance();

            f.getExternalContext().getFlash().setKeepMessages(true);
            f.addMessage(null, message);
            return "Signin?faces-redirect=true";
        }

    }
    
    private UserAuthDTO authenticateUser (UserAuthDTO userAuthDTO) {
        userAuthDTO.setResponseCode(TickerResponseCode.PASSWORD_INCORRECT);
        if (userAuthDTO.getUserId().equals("susmita")) {
            if (userAuthDTO.getPassword().equals("bumbu123")) {
                userAuthDTO.setResponseCode(TickerResponseCode.SUCCESS);
            }
        }
        return userAuthDTO;
    }



    public String moveToDefaultHost() {
//        productID = 2;
//
//        String hedwigServer = context.getInitParameter("HedwigServerName");
//        String hedwigServerPort = context.getInitParameter("HedwigServerPort");
//        TenantListClient dgrftlc = new TenantListClient(hedwigServer,hedwigServerPort);
//        List<TenantDTO> tenantDTOs = dgrftlc.getTenantList(productID);
//        if (tenantDTOs == null) {
//            return "/access.xhtml?faces-redirect=true";
//        }
//
//        if (tenantID == 0) {
//            tenantID = 1;
//        }
//        List<TenantDTO> tenantDTOMatched = tenantDTOs.stream().filter(tenant -> tenant.getTenantId() == tenantID).collect(Collectors.toList());
//        //setAuthCredentials();
//        if (tenantDTOMatched.isEmpty()) {
//            return "/access.xhtml?faces-redirect=true";
//        }
//        tenantName = tenantDTOMatched.get(0).getName();
        return "/Landing.xhtml?faces-redirect=true";

    }
    
    
//    private void setAuthCredentials () {
//        HedwigAuthCredentials authCredentials = new HedwigAuthCredentials();
//        authCredentials.setUserId(userAuthDTO.getUserId());
//        authCredentials.setPassword(userAuthDTO.getPassword());
//        authCredentials.setProductId(productID);
//        authCredentials.setTenantId(tenantID);
//        authCredentials.setHedwigServer(userAuthDTO.getHedwigServer());
//        authCredentials.setHedwigServerPort(userAuthDTO.getHedwigServerPort());
//        TaalMaanAuthCredtialValue.AUTH_CREDENTIALS = authCredentials;
//    }

    public String logout() {
        userAuthDTO.setUserId(null);
//        userAuthDTO.setDbConnUrl(null);
//        userAuthDTO.setName(null);

        return moveToDefaultHost();
    }



    public UserAuthDTO getUserAuthDTO() {
        return userAuthDTO;
    }

    public void setUserAuthDTO(UserAuthDTO userAuthDTO) {
        this.userAuthDTO = userAuthDTO;
    }

    
    public String getUserID() {
        return userID;
    }



    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
