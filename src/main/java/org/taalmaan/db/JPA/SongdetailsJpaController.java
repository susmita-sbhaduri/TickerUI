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
import org.taalmaan.db.entities.Songdetails;

/**
 *
 * @author bhaduri
 */
public class SongdetailsJpaController implements Serializable {

    public SongdetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Songdetails songdetails) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(songdetails);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSongdetails(songdetails.getSongid()) != null) {
                throw new PreexistingEntityException("Songdetails " + songdetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Songdetails songdetails) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            songdetails = em.merge(songdetails);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = songdetails.getSongid();
                if (findSongdetails(id) == null) {
                    throw new NonexistentEntityException("The songdetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Songdetails songdetails;
            try {
                songdetails = em.getReference(Songdetails.class, id);
                songdetails.getSongid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The songdetails with id " + id + " no longer exists.", enfe);
            }
            em.remove(songdetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Songdetails> findSongdetailsEntities() {
        return findSongdetailsEntities(true, -1, -1);
    }

    public List<Songdetails> findSongdetailsEntities(int maxResults, int firstResult) {
        return findSongdetailsEntities(false, maxResults, firstResult);
    }

    private List<Songdetails> findSongdetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Songdetails.class));
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

    public Songdetails findSongdetails(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Songdetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getSongdetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Songdetails> rt = cq.from(Songdetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
