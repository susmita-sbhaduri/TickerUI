/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaanservices.JPA;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.dgrf.taalmaanservices.JPA.exceptions.NonexistentEntityException;
import org.dgrf.taalmaanservices.JPA.exceptions.PreexistingEntityException;
import org.dgrf.taalmaanservices.entities.Taalparam;
import org.dgrf.taalmaanservices.entities.TaalparamPK;

/**
 *
 * @author bhaduri
 */
public class TaalparamJpaController implements Serializable {

    public TaalparamJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Taalparam taalparam) throws PreexistingEntityException, Exception {
        if (taalparam.getTaalparamPK() == null) {
            taalparam.setTaalparamPK(new TaalparamPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(taalparam);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTaalparam(taalparam.getTaalparamPK()) != null) {
                throw new PreexistingEntityException("Taalparam " + taalparam + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Taalparam taalparam) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            taalparam = em.merge(taalparam);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TaalparamPK id = taalparam.getTaalparamPK();
                if (findTaalparam(id) == null) {
                    throw new NonexistentEntityException("The taalparam with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TaalparamPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Taalparam taalparam;
            try {
                taalparam = em.getReference(Taalparam.class, id);
                taalparam.getTaalparamPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The taalparam with id " + id + " no longer exists.", enfe);
            }
            em.remove(taalparam);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Taalparam> findTaalparamEntities() {
        return findTaalparamEntities(true, -1, -1);
    }

    public List<Taalparam> findTaalparamEntities(int maxResults, int firstResult) {
        return findTaalparamEntities(false, maxResults, firstResult);
    }

    private List<Taalparam> findTaalparamEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Taalparam.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Taalparam findTaalparam(TaalparamPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Taalparam.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaalparamCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Taalparam> rt = cq.from(Taalparam.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
