/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aleksa
 */
@Entity
@Table(name = "potkategorija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Potkategorija.findAll", query = "SELECT p FROM Potkategorija p"),
    @NamedQuery(name = "Potkategorija.findByIdPotKat", query = "SELECT p FROM Potkategorija p WHERE p.idPotKat = :idPotKat")})
public class Potkategorija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdPotKat")
    private Integer idPotKat;
    @JoinColumn(name = "IdNadKat", referencedColumnName = "IdKat")
    @ManyToOne(optional = false)
    private Kategorija idNadKat;
    @JoinColumn(name = "IdPotKat", referencedColumnName = "IdKat", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Kategorija kategorija;

    public Potkategorija() {
    }

    public Potkategorija(Integer idPotKat) {
        this.idPotKat = idPotKat;
    }

    public Integer getIdPotKat() {
        return idPotKat;
    }

    public void setIdPotKat(Integer idPotKat) {
        this.idPotKat = idPotKat;
    }

    public Kategorija getIdNadKat() {
        return idNadKat;
    }

    public void setIdNadKat(Kategorija idNadKat) {
        this.idNadKat = idNadKat;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPotKat != null ? idPotKat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Potkategorija)) {
            return false;
        }
        Potkategorija other = (Potkategorija) object;
        if ((this.idPotKat == null && other.idPotKat != null) || (this.idPotKat != null && !this.idPotKat.equals(other.idPotKat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Potkategorija[ idPotKat=" + idPotKat + " ]";
    }
    
}
