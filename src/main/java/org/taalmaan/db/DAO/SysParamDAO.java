/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.db.DAO;

import javax.persistence.EntityManagerFactory;
import org.taalmaan.db.JPA.TaalsysparamsJpaController;

/**
 *
 * @author bhaduri
 */
public class SysParamDAO extends TaalsysparamsJpaController{

    public SysParamDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
    
}
