package entiteti;

import entiteti.Stavka;
import entiteti.Transakcija;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-19T19:41:51")
@StaticMetamodel(Narudzbina.class)
public class Narudzbina_ { 

    public static volatile SingularAttribute<Narudzbina, Integer> idNar;
    public static volatile SingularAttribute<Narudzbina, Date> vreme;
    public static volatile SingularAttribute<Narudzbina, Integer> idKor;
    public static volatile SingularAttribute<Narudzbina, String> adresa;
    public static volatile SingularAttribute<Narudzbina, Integer> idGrad;
    public static volatile ListAttribute<Narudzbina, Stavka> stavkaList;
    public static volatile ListAttribute<Narudzbina, Transakcija> transakcijaList;
    public static volatile SingularAttribute<Narudzbina, Double> cena;

}