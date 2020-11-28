/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taalmaan.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bhaduri
 */
@Entity
@Table(name = "taalsysparams")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taalsysparams.findAll", query = "SELECT t FROM Taalsysparams t")})
public class Taalsysparams implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "taalkey")
    private String taalkey;
    @Size(max = 200)
    @Column(name = "taalvalue")
    private String taalvalue;

    public Taalsysparams() {
    }

    public Taalsysparams(String taalkey) {
        this.taalkey = taalkey;
    }

    public String getTaalkey() {
        return taalkey;
    }

    public void setTaalkey(String taalkey) {
        this.taalkey = taalkey;
    }

    public String getTaalvalue() {
        return taalvalue;
    }

    public void setTaalvalue(String taalvalue) {
        this.taalvalue = taalvalue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taalkey != null ? taalkey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taalsysparams)) {
            return false;
        }
        Taalsysparams other = (Taalsysparams) object;
        if ((this.taalkey == null && other.taalkey != null) || (this.taalkey != null && !this.taalkey.equals(other.taalkey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.taalmaan.db.entities.Taalsysparams[ taalkey=" + taalkey + " ]";
    }
    
}
