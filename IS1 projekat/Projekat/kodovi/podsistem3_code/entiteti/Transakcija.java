/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aleksa
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTran", query = "SELECT t FROM Transakcija t WHERE t.idTran = :idTran"),
    @NamedQuery(name = "Transakcija.findBySuma", query = "SELECT t FROM Transakcija t WHERE t.suma = :suma"),
    @NamedQuery(name = "Transakcija.findByVreme", query = "SELECT t FROM Transakcija t WHERE t.vreme = :vreme")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdTran")
    private Integer idTran;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Suma")
    private double suma;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Vreme")
    @Temporal(TemporalType.TIME)
    private Date vreme;
    @JoinColumn(name = "IdNarTran", referencedColumnName = "IdNar")
    @ManyToOne(optional = false)
    private Narudzbina idNarTran;

    public Transakcija() {
    }

    public Transakcija(Integer idTran) {
        this.idTran = idTran;
    }

    public Transakcija(Integer idTran, double suma, Date vreme) {
        this.idTran = idTran;
        this.suma = suma;
        this.vreme = vreme;
    }

    public Integer getIdTran() {
        return idTran;
    }

    public void setIdTran(Integer idTran) {
        this.idTran = idTran;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public Narudzbina getIdNarTran() {
        return idNarTran;
    }

    public void setIdNarTran(Narudzbina idNarTran) {
        this.idNarTran = idNarTran;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTran != null ? idTran.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTran == null && other.idTran != null) || (this.idTran != null && !this.idTran.equals(other.idTran))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Transakcija[ idTran=" + idTran + " ]";
    }
    
}
