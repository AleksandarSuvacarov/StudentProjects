/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Aleksa
 */
@Entity
@Table(name = "korpa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Korpa.findAll", query = "SELECT k FROM Korpa k"),
    @NamedQuery(name = "Korpa.findByIdKorp", query = "SELECT k FROM Korpa k WHERE k.idKorp = :idKorp"),
    @NamedQuery(name = "Korpa.findByIdKor", query = "SELECT k FROM Korpa k WHERE k.idKor = :idKor"),
    @NamedQuery(name = "Korpa.findByCena", query = "SELECT k FROM Korpa k WHERE k.cena = :cena")})
public class Korpa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdKorp")
    private Integer idKorp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private double cena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "korpa")
    private List<SeNalazi> seNalaziList;

    public Korpa() {
    }

    public Korpa(Integer idKorp) {
        this.idKorp = idKorp;
    }

    public Korpa(Integer idKorp, int idKor, double cena) {
        this.idKorp = idKorp;
        this.idKor = idKor;
        this.cena = cena;
    }

    public Integer getIdKorp() {
        return idKorp;
    }

    public void setIdKorp(Integer idKorp) {
        this.idKorp = idKorp;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    @XmlTransient
    public List<SeNalazi> getSeNalaziList() {
        return seNalaziList;
    }

    public void setSeNalaziList(List<SeNalazi> seNalaziList) {
        this.seNalaziList = seNalaziList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKorp != null ? idKorp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Korpa)) {
            return false;
        }
        Korpa other = (Korpa) object;
        if ((this.idKorp == null && other.idKorp != null) || (this.idKorp != null && !this.idKorp.equals(other.idKorp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Korpa[ idKorp=" + idKorp + " ]";
    }
    
}
