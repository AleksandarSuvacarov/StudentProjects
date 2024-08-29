package entiteti;

import entiteti.SeNalazi;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-19T19:41:50")
@StaticMetamodel(Korpa.class)
public class Korpa_ { 

    public static volatile SingularAttribute<Korpa, Integer> idKor;
    public static volatile SingularAttribute<Korpa, Double> cena;
    public static volatile ListAttribute<Korpa, SeNalazi> seNalaziList;
    public static volatile SingularAttribute<Korpa, Integer> idKorp;

}