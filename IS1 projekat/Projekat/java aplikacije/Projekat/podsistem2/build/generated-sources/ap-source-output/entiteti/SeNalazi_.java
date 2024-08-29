package entiteti;

import entiteti.Artikal;
import entiteti.Korpa;
import entiteti.SeNalaziPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-19T19:41:50")
@StaticMetamodel(SeNalazi.class)
public class SeNalazi_ { 

    public static volatile SingularAttribute<SeNalazi, SeNalaziPK> seNalaziPK;
    public static volatile SingularAttribute<SeNalazi, Korpa> korpa;
    public static volatile SingularAttribute<SeNalazi, Artikal> artikal;
    public static volatile SingularAttribute<SeNalazi, Integer> kolicina;

}