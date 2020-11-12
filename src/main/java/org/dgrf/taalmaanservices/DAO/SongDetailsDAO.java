/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaanservices.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import org.dgrf.taalmaanservices.JPA.SongdetailsJpaController;

/**
 *
 * @author dgrf-vi
 */
public class SongDetailsDAO extends SongdetailsJpaController {

    public SongDetailsDAO(EntityManagerFactory emf) {
        super(emf);
    }

    public int getMaxSongId() {
        EntityManager em = getEntityManager();
        int m;
        try {
            TypedQuery<Integer> query = em.createNamedQuery("Songdetails.findMaxSongId", Integer.class);
            m = query.getSingleResult();
            return m;
        } catch (NullPointerException e) {
            return 0;
        }
    }

}
