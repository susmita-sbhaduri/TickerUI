/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.controller;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.hedwig.cloud.client.TenantListClient;
import org.hedwig.cloud.client.UserAuthClient;
import org.hedwig.cloud.dto.HedwigAuthCredentials;
import org.hedwig.cloud.dto.TenantDTO;
import org.hedwig.cloud.dto.UserAuthDTO;
import org.hedwig.cloud.response.HedwigResponseCode;

import org.taalmaan.utils.TaalMaanAuthCredtialValue;

/**
 *
 * @author dgrf-iv
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private int tenantID;
    private String tenantName;
    private int productID;
    private String userID;
    private String password;
    private UserAuthDTO userAuthDTO;
    private List<SelectItem> tenantMap;

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
        TenantListClient dgrftlc = new TenantListClient();
        List<TenantDTO> tenantDTOs = dgrftlc.getTenantList(productID);
        tenantMap = tenantDTOs.stream().map(tenant -> {
            SelectItem selectItem = new SelectItem(tenant.getTenantId(), tenant.getName());
            return selectItem;
        }).collect(Collectors.toList());

    }

    public String login() {

        UserAuthClient uac = new UserAuthClient();
        userAuthDTO.setUserId(userID);
        userAuthDTO.setPassword(password);
        userAuthDTO.setProductId(productID);
        userAuthDTO.setTenantId(tenantID);
        userAuthDTO = uac.authenticateUser(userAuthDTO);
        FacesMessage message;
        
        if (userAuthDTO.getResponseCode() == HedwigResponseCode.SUCCESS) {
            setAuthCredentials();
            return "SonglistUser?faces-redirect=true";
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", userAuthDTO.getResponseMessage());
            FacesContext f = FacesContext.getCurrentInstance();

            f.getExternalContext().getFlash().setKeepMessages(true);
            f.addMessage(null, message);
            return "Signin?faces-redirect=true";
        }

    }

    public String selectTenant() {
        return "/index?faces-redirect=true&tenant=" + tenantID;
    }

    public String moveToDefaultHost() {
        productID = 2;

        TenantListClient dgrftlc = new TenantListClient();
        List<TenantDTO> tenantDTOs = dgrftlc.getTenantList(productID);
        if (tenantDTOs == null) {
            return "/access.xhtml?faces-redirect=true";
        }

        if (tenantID == 0) {
            tenantID = 1;
        }
        List<TenantDTO> tenantDTOMatched = tenantDTOs.stream().filter(tenant -> tenant.getTenantId() == tenantID).collect(Collectors.toList());
        setAuthCredentials();
        if (tenantDTOMatched.isEmpty()) {
            return "/access.xhtml?faces-redirect=true";
        }
        tenantName = tenantDTOMatched.get(0).getName();
        return "/Landing.xhtml?faces-redirect=true";

    }
    
    private void setAuthCredentials () {
        HedwigAuthCredentials authCredentials = new HedwigAuthCredentials();
        authCredentials.setUserId(userAuthDTO.getUserId());
        authCredentials.setPassword(userAuthDTO.getPassword());
        authCredentials.setProductId(productID);
        authCredentials.setTenantId(tenantID);
        TaalMaanAuthCredtialValue.AUTH_CREDENTIALS = authCredentials;
    }

    public String logout() {
        userAuthDTO.setUserId(null);
        userAuthDTO.setDbConnUrl(null);
        userAuthDTO.setName(null);

        return moveToDefaultHost();
    }

    public void goToUserRegister() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("Host");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String urlPrefix = request.getScheme();
        
        String redirectUrl = urlPrefix + "://"+ipAddress+"/" + "DGRFCloud/faces/UserRegister.xhtml?tenant="+tenantID+"&product="+productID;
        //System.out.println("IP redirectUrl:" +redirectUrl);
         
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(redirectUrl);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public UserAuthDTO getUserAuthDTO() {
        return userAuthDTO;
    }

    public void setUserAuthDTO(UserAuthDTO userAuthDTO) {
        this.userAuthDTO = userAuthDTO;
    }

    public List<SelectItem> getTenantMap() {
        return tenantMap;
    }

    public void setTenantMap(List<SelectItem> tenantMap) {
        this.tenantMap = tenantMap;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getUserID() {
        return userID;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
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
