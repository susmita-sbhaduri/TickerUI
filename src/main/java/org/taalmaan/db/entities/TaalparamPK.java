/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bhaduri
 */
@Embeddable
public class TaalparamPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "taal")
    private String taal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paramid")
    private int paramid;

    public TaalparamPK() {
    }

    public TaalparamPK(String taal, int paramid) {
        this.taal = taal;
        this.paramid = paramid;
    }

    public String getTaal() {
        return taal;
    }

    public void setTaal(String taal) {
        this.taal = taal;
    }

    public int getParamid() {
        return paramid;
    }

    public void setParamid(int paramid) {
        this.paramid = paramid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taal != null ? taal.hashCode() : 0);
        hash += (int) paramid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaalparamPK)) {
            return false;
        }
        TaalparamPK other = (TaalparamPK) object;
        if ((this.taal == null && other.taal != null) || (this.taal != null && !this.taal.equals(other.taal))) {
            return false;
        }
        if (this.paramid != other.paramid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.taalmaan.db.entities.TaalparamPK[ taal=" + taal + ", paramid=" + paramid + " ]";
    }
    
}
