/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.db.JPA;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.taalmaan.db.JPA.exceptions.NonexistentEntityException;
import org.taalmaan.db.JPA.exceptions.PreexistingEntityException;
import org.taalmaan.db.entities.Taalsysparams;

/**
 *
 * @author bhaduri
 */
public class TaalsysparamsJpaController implements Serializable {

    public TaalsysparamsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Taalsysparams taalsysparams) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(taalsysparams);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTaalsysparams(taalsysparams.getTaalkey()) != null) {
                throw new PreexistingEntityException("Taalsysparams " + taalsysparams + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Taalsysparams taalsysparams) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            taalsysparams = em.merge(taalsysparams);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = taalsysparams.getTaalkey();
                if (findTaalsysparams(id) == null) {
                    throw new NonexistentEntityException("The taalsysparams with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Taalsysparams taalsysparams;
            try {
                taalsysparams = em.getReference(Taalsysparams.class, id);
                taalsysparams.getTaalkey();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The taalsysparams with id " + id + " no longer exists.", enfe);
            }
            em.remove(taalsysparams);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Taalsysparams> findTaalsysparamsEntities() {
        return findTaalsysparamsEntities(true, -1, -1);
    }

    public List<Taalsysparams> findTaalsysparamsEntities(int maxResults, int firstResult) {
        return findTaalsysparamsEntities(false, maxResults, firstResult);
    }

    private List<Taalsysparams> findTaalsysparamsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Taalsysparams.class));
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

    public Taalsysparams findTaalsysparams(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Taalsysparams.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaalsysparamsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Taalsysparams> rt = cq.from(Taalsysparams.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
