/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaanservices.DAO;

import javax.persistence.EntityManagerFactory;
import org.dgrf.taalmaanservices.JPA.TaalparamJpaController;

/**
 *
 * @author bhaduri
 */
public class TaalparamDAO extends TaalparamJpaController{

    public TaalparamDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
    
}
