package entiteti;

import entiteti.Kategorija;
import entiteti.Recenzija;
import entiteti.SeNalazi;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-19T19:41:50")
@StaticMetamodel(Artikal.class)
public class Artikal_ { 

    public static volatile SingularAttribute<Artikal, Integer> idKor;
    public static volatile SingularAttribute<Artikal, Kategorija> idKat;
    public static volatile SingularAttribute<Artikal, Recenzija> recenzija;
    public static volatile SingularAttribute<Artikal, String> naziv;
    public static volatile SingularAttribute<Artikal, Double> popust;
    public static volatile SingularAttribute<Artikal, Double> cena;
    public static volatile ListAttribute<Artikal, SeNalazi> seNalaziList;
    public static volatile SingularAttribute<Artikal, Integer> idArt;
    public static volatile SingularAttribute<Artikal, String> opis;

}