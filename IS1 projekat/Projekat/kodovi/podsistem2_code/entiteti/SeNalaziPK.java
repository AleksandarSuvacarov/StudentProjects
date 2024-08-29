/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Aleksa
 */
@Embeddable
public class SeNalaziPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdSeKorp")
    private int idSeKorp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdSeArt")
    private int idSeArt;

    public SeNalaziPK() {
    }

    public SeNalaziPK(int idSeKorp, int idSeArt) {
        this.idSeKorp = idSeKorp;
        this.idSeArt = idSeArt;
    }

    public int getIdSeKorp() {
        return idSeKorp;
    }

    public void setIdSeKorp(int idSeKorp) {
        this.idSeKorp = idSeKorp;
    }

    public int getIdSeArt() {
        return idSeArt;
    }

    public void setIdSeArt(int idSeArt) {
        this.idSeArt = idSeArt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSeKorp;
        hash += (int) idSeArt;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeNalaziPK)) {
            return false;
        }
        SeNalaziPK other = (SeNalaziPK) object;
        if (this.idSeKorp != other.idSeKorp) {
            return false;
        }
        if (this.idSeArt != other.idSeArt) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.SeNalaziPK[ idSeKorp=" + idSeKorp + ", idSeArt=" + idSeArt + " ]";
    }
    
}
