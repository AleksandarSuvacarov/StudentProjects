/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aleksa
 */
@Entity
@Table(name = "se_nalazi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeNalazi.findAll", query = "SELECT s FROM SeNalazi s"),
    @NamedQuery(name = "SeNalazi.findByIdSeKorp", query = "SELECT s FROM SeNalazi s WHERE s.seNalaziPK.idSeKorp = :idSeKorp"),
    @NamedQuery(name = "SeNalazi.findByIdSeArt", query = "SELECT s FROM SeNalazi s WHERE s.seNalaziPK.idSeArt = :idSeArt"),
    @NamedQuery(name = "SeNalazi.findByKolicina", query = "SELECT s FROM SeNalazi s WHERE s.kolicina = :kolicina")})
public class SeNalazi implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SeNalaziPK seNalaziPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kolicina")
    private int kolicina;
    @JoinColumn(name = "IdSeArt", referencedColumnName = "IdArt", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Artikal artikal;
    @JoinColumn(name = "IdSeKorp", referencedColumnName = "IdKorp", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Korpa korpa;

    public SeNalazi() {
    }

    public SeNalazi(SeNalaziPK seNalaziPK) {
        this.seNalaziPK = seNalaziPK;
    }

    public SeNalazi(SeNalaziPK seNalaziPK, int kolicina) {
        this.seNalaziPK = seNalaziPK;
        this.kolicina = kolicina;
    }

    public SeNalazi(int idSeKorp, int idSeArt) {
        this.seNalaziPK = new SeNalaziPK(idSeKorp, idSeArt);
    }

    public SeNalaziPK getSeNalaziPK() {
        return seNalaziPK;
    }

    public void setSeNalaziPK(SeNalaziPK seNalaziPK) {
        this.seNalaziPK = seNalaziPK;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public Artikal getArtikal() {
        return artikal;
    }

    public void setArtikal(Artikal artikal) {
        this.artikal = artikal;
    }

    public Korpa getKorpa() {
        return korpa;
    }

    public void setKorpa(Korpa korpa) {
        this.korpa = korpa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seNalaziPK != null ? seNalaziPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeNalazi)) {
            return false;
        }
        SeNalazi other = (SeNalazi) object;
        if ((this.seNalaziPK == null && other.seNalaziPK != null) || (this.seNalaziPK != null && !this.seNalaziPK.equals(other.seNalaziPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.SeNalazi[ seNalaziPK=" + seNalaziPK + " ]";
    }
    
}
