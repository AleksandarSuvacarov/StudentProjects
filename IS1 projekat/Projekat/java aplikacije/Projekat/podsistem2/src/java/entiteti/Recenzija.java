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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aleksa
 */
@Entity
@Table(name = "recenzija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recenzija.findAll", query = "SELECT r FROM Recenzija r"),
    @NamedQuery(name = "Recenzija.findByIdRecArt", query = "SELECT r FROM Recenzija r WHERE r.idRecArt = :idRecArt"),
    @NamedQuery(name = "Recenzija.findByOcena", query = "SELECT r FROM Recenzija r WHERE r.ocena = :ocena"),
    @NamedQuery(name = "Recenzija.findByOpis", query = "SELECT r FROM Recenzija r WHERE r.opis = :opis")})
public class Recenzija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdRecArt")
    private Integer idRecArt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Ocena")
    private int ocena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Opis")
    private String opis;
    @JoinColumn(name = "IdRecArt", referencedColumnName = "IdArt", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Artikal artikal;

    public Recenzija() {
    }

    public Recenzija(Integer idRecArt) {
        this.idRecArt = idRecArt;
    }

    public Recenzija(Integer idRecArt, int ocena, String opis) {
        this.idRecArt = idRecArt;
        this.ocena = ocena;
        this.opis = opis;
    }

    public Integer getIdRecArt() {
        return idRecArt;
    }

    public void setIdRecArt(Integer idRecArt) {
        this.idRecArt = idRecArt;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Artikal getArtikal() {
        return artikal;
    }

    public void setArtikal(Artikal artikal) {
        this.artikal = artikal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecArt != null ? idRecArt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recenzija)) {
            return false;
        }
        Recenzija other = (Recenzija) object;
        if ((this.idRecArt == null && other.idRecArt != null) || (this.idRecArt != null && !this.idRecArt.equals(other.idRecArt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Recenzija[ idRecArt=" + idRecArt + " ]";
    }
    
}
