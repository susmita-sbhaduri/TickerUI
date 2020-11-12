/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgrf.taalmaanservices.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bhaduri
 */
@Entity
@Table(name = "taalparam")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taalparam.findAll", query = "SELECT t FROM Taalparam t")})
public class Taalparam implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TaalparamPK taalparamPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "firststrokecount")
    private int firststrokecount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "secondstrokecount")
    private int secondstrokecount;

    public Taalparam() {
    }

    public Taalparam(TaalparamPK taalparamPK) {
        this.taalparamPK = taalparamPK;
    }

    public Taalparam(TaalparamPK taalparamPK, int firststrokecount, int secondstrokecount) {
        this.taalparamPK = taalparamPK;
        this.firststrokecount = firststrokecount;
        this.secondstrokecount = secondstrokecount;
    }

    public Taalparam(String taal, int paramid) {
        this.taalparamPK = new TaalparamPK(taal, paramid);
    }

    public TaalparamPK getTaalparamPK() {
        return taalparamPK;
    }

    public void setTaalparamPK(TaalparamPK taalparamPK) {
        this.taalparamPK = taalparamPK;
    }

    public int getFirststrokecount() {
        return firststrokecount;
    }

    public void setFirststrokecount(int firststrokecount) {
        this.firststrokecount = firststrokecount;
    }

    public int getSecondstrokecount() {
        return secondstrokecount;
    }

    public void setSecondstrokecount(int secondstrokecount) {
        this.secondstrokecount = secondstrokecount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taalparamPK != null ? taalparamPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taalparam)) {
            return false;
        }
        Taalparam other = (Taalparam) object;
        if ((this.taalparamPK == null && other.taalparamPK != null) || (this.taalparamPK != null && !this.taalparamPK.equals(other.taalparamPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dgrf.taalmaanservices.entities.Taalparam[ taalparamPK=" + taalparamPK + " ]";
    }
    
}
