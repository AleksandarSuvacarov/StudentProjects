package entiteti;

import entiteti.Artikal;
import entiteti.Potkategorija;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-19T19:41:50")
@StaticMetamodel(Kategorija.class)
public class Kategorija_ { 

    public static volatile SingularAttribute<Kategorija, Integer> idKat;
    public static volatile SingularAttribute<Kategorija, String> naziv;
    public static volatile ListAttribute<Kategorija, Potkategorija> potkategorijaList;
    public static volatile ListAttribute<Kategorija, Artikal> artikalList;
    public static volatile SingularAttribute<Kategorija, Potkategorija> potkategorija;

}