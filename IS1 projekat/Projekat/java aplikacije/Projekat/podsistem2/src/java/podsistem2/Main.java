/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Queue;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Aleksa
 */
public class Main {

    private static EntityManager em;
    
//    private static EntityManager em = null;
    
    @Resource(lookup="mojaFabrika")
    private static ConnectionFactory fabrika;
    
    @Resource(lookup="mojTopic1")
    private static Topic topic1;
    
    @Resource(lookup="mojRed2")
    private static Queue red1;
    
    @Resource(lookup="mojRed3")
    private static Queue red_transakcije;
    
    @Resource(lookup="mojTopic2")
    private static Topic topic2;
    
    private static JMSContext context = null;
    private static JMSConsumer consumer = null;
    private static JMSProducer producer = null;
    
    
    private static void kreiraj_kategoriju(TextMessage zahtev) throws JMSException{
        
        String naziv = zahtev.getStringProperty("naziv");

        List<Kategorija> rl = em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv = :naziv", Kategorija.class).setParameter("naziv", naziv).getResultList();
        if(rl != null && !rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Kategorija vec postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }

        Kategorija kat = new Kategorija();
        kat.setNaziv(naziv);
        em.getTransaction().begin();
        em.persist(kat);
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno dodata kategorija");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
    }
    
    private static void kreiraj_artikal(TextMessage zahtev) throws JMSException{
        
        String naziv = zahtev.getStringProperty("naziv");
        String opis = zahtev.getStringProperty("opis");
        double cena = zahtev.getDoubleProperty("cena");
        double popust = zahtev.getDoubleProperty("popust");
        String kategorija = zahtev.getStringProperty("kategorija");
        int idKor = zahtev.getIntProperty("idKor");
       

        List<Kategorija> rl = em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv = :naziv", Kategorija.class).setParameter("naziv", kategorija).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Kategorija ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }

        Artikal art = new Artikal();
        art.setCena(cena);
        art.setIdKat(rl.get(0));
        art.setNaziv(naziv);
        art.setOpis(opis);
        art.setPopust(popust);
        art.setIdKor(idKor);
        
        em.getTransaction().begin();
        em.persist(art);
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno dodat artikal");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
    }
    
    private static void menjaj_cenu_artikla(TextMessage zahtev) throws JMSException{
        
        String naziv = zahtev.getStringProperty("naziv");
        double cena = zahtev.getDoubleProperty("cena");

        List<Artikal> rl = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class).setParameter("naziv", naziv).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Artikal ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        Artikal a = rl.get(0);
        
        em.getTransaction().begin();
        a.setCena(cena);
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno promenjena cena");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
    }
    
    private static void menjaj_popust_artikla(TextMessage zahtev) throws JMSException{
        
        String naziv = zahtev.getStringProperty("naziv");
        double popust = zahtev.getDoubleProperty("popust");

        List<Artikal> rl = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class).setParameter("naziv", naziv).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Artikal ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        Artikal a = rl.get(0);
        
        em.getTransaction().begin();
        a.setPopust(popust);
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno promenjen popust");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
    }
    
    private static void dodaj_u_korpu(TextMessage zahtev) throws JMSException{
        int idKor = zahtev.getIntProperty("idKor");
        String artikal = zahtev.getStringProperty("artikal");
        int kolicina = zahtev.getIntProperty("kolicina");
        
        List<Artikal> rl = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class).setParameter("naziv", artikal).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Artikal ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        Artikal a = rl.get(0);
               
        em.getTransaction().begin();
        List<Korpa> rl1 = em.createQuery("SELECT k FROM Korpa k WHERE k.idKor = :idKor", Korpa.class).setParameter("idKor", idKor).getResultList();
        Korpa k = null;
        if(rl1 == null || rl1.isEmpty()){
            k = new Korpa();
            k.setIdKor(idKor);
            k.setCena(0);
            em.persist(k);
            em.flush();
        }
        else{
            k = rl1.get(0);
        }    
        
        SeNalaziPK kljuc = new SeNalaziPK(k.getIdKorp(), a.getIdArt());
        SeNalazi find = em.find(SeNalazi.class, kljuc);
        if(find == null){
            SeNalazi novi = new SeNalazi(kljuc);
            novi.setKolicina(kolicina);
            novi.setArtikal(a);
            novi.setKorpa(k);
            em.persist(novi);
        }
        else{
            find.setKolicina(find.getKolicina() + kolicina);
        }
        

        double temp = k.getCena() + kolicina * (a.getCena() * (1 - a.getPopust()));
        k.setCena(temp);
 
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno dodat artikal");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
    }
    
    private static void brisi_iz_korpe(TextMessage zahtev) throws JMSException{
        int idKor = zahtev.getIntProperty("idKor");
        String artikal = zahtev.getStringProperty("artikal");
        int kolicina = zahtev.getIntProperty("kolicina");
        
        List<Korpa> rl1 = em.createQuery("SELECT k FROM Korpa k WHERE k.idKor = :idKor", Korpa.class).setParameter("idKor", idKor).getResultList();
        if(rl1 == null || rl1.isEmpty()){
            TextMessage txt = context.createTextMessage("Korpa ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }          
        
        List<Artikal> rl = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class).setParameter("naziv", artikal).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Artikal ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        Artikal a = rl.get(0);
        Korpa k = rl1.get(0);
               
        em.getTransaction().begin();
                
        
        SeNalaziPK kljuc = new SeNalaziPK(k.getIdKorp(), a.getIdArt());
        SeNalazi find = em.find(SeNalazi.class, kljuc);
        if(find == null){
            TextMessage txt = context.createTextMessage("Artikal nije u korpi");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        
        int kolicina_stara = find.getKolicina();
        if(kolicina_stara <= kolicina){
            em.remove(find);
            double temp = k.getCena() - kolicina_stara * (a.getCena() * (1 - a.getPopust()));
            k.setCena(temp);
        }
        else{
            find.setKolicina(kolicina_stara - kolicina);
            double temp = k.getCena() - kolicina * (a.getCena() * (1 - a.getPopust()));
            k.setCena(temp);
        }
        
        
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno obrisan artikal");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
    }
    
    private static void dohvati_sve_kategorije(TextMessage zahtev) throws JMSException  {
        List<Kategorija> rl = em.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
                
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Nema kategorija");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }
        
        String str = rl.get(0).getIdKat().toString() + "-" + rl.get(0).getNaziv();
        for(int i = 1; i < rl.size(); i++){
            str = str + "\n" +rl.get(i).getIdKat().toString() + "-" + rl.get(i).getNaziv();
        }
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
        
    }
    
    private static void dohvati_moje_artikle_koje_prodajem(TextMessage zahtev) throws JMSException  {
        List<Artikal> rl = em.createNamedQuery("Artikal.findByIdKor", Artikal.class).setParameter("idKor", zahtev.getIntProperty("idKor")).getResultList();
        
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Korisnik ne prodaje ni jedan artikal");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }

        Artikal art = rl.get(0);
        String str = art.getIdArt() +  "-" + art.getNaziv() + "-" + art.getOpis() + "-" + art.getIdKat().getNaziv() + "-" + art.getCena() + "-" + art.getPopust() * 100 + "%";
        for(int i = 1; i < rl.size(); i++){
            art = rl.get(i);
            str = str + "\n" + art.getIdArt() +  "-" + art.getNaziv() + "-" + art.getOpis() + "-" + art.getIdKat().getNaziv() + "-" + art.getCena() + "-" + art.getPopust() * 100 + "%";
        }


        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
        
    }
    
    private static void dohvati_moje_artikle_iz_korpe(TextMessage zahtev) throws JMSException  {
        List<Korpa> rl = em.createNamedQuery("Korpa.findByIdKor", Korpa.class).setParameter("idKor", zahtev.getIntProperty("idKor")).getResultList();
        
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Korisnik nema korpu");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }
        Korpa korpa = rl.get(0);
        
        List<SeNalazi> rl1 = em.createNamedQuery("SeNalazi.findByIdSeKorp", SeNalazi.class).setParameter("idSeKorp", korpa.getIdKorp()).getResultList();
        
        if(rl1 == null || rl1.isEmpty()){
            TextMessage txt = context.createTextMessage("Korisnik nema ni jedan artikal u korpi");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }


        String str = rl1.get(0).getArtikal().getNaziv() + "-" + rl1.get(0).getKolicina();
        for(int i = 1; i < rl1.size(); i++){
            str = str + "\n" + rl1.get(i).getArtikal().getNaziv() + "-" + rl1.get(i).getKolicina();
        }
        str = str + "\nCena korpe: " + korpa.getCena();
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
        
    }
    
    private static void dohvati_korpu_sa_artiklima(TextMessage zahtev) throws JMSException{
        
        int idKor = zahtev.getIntProperty("idKor");
        
        List<Korpa> rl = em.createQuery("SELECT k FROM Korpa k WHERE k.idKor = :idKor", Korpa.class).setParameter("idKor", idKor).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Korpa ne postoji");
            txt.setIntProperty("kod", 400);
            txt.setIntProperty("found", 0);
            producer.send(red_transakcije, txt);
            return;
        } 
        Korpa korpa = rl.get(0);
        
        List<SeNalazi> rl1 = em.createNamedQuery("SeNalazi.findByIdSeKorp", SeNalazi.class).setParameter("idSeKorp", korpa.getIdKorp()).getResultList();
        
        if(rl1 == null || rl1.isEmpty()){
            TextMessage txt = context.createTextMessage("Korisnik nema ni jedan artikal u korpi");
            txt.setIntProperty("kod", 400);
            txt.setIntProperty("found", 0);
            producer.send(red_transakcije, txt);
            return;
        }
        TextMessage txt = context.createTextMessage("Korpa postoji");
        txt.setIntProperty("velicina", rl1.size());
        txt.setIntProperty("kod", 200);
        txt.setIntProperty("found", 1);
        producer.send(red_transakcije, txt);
        
        for(int i = 0; i < rl1.size(); i++){
            Artikal art = rl1.get(i).getArtikal();
            TextMessage artMsg = context.createTextMessage();
            artMsg.setIntProperty("idArt", art.getIdArt());
            artMsg.setIntProperty("kolicina", rl1.get(i).getKolicina());
            artMsg.setDoubleProperty("cena", art.getCena() * (1 - art.getPopust()));
            artMsg.setIntProperty("idKor", art.getIdKor());
            producer.send(red_transakcije, artMsg);
        }
        
        em.getTransaction().begin();
        
        for(SeNalazi sn : rl1){
            em.remove(sn);
        }
        
        em.remove(korpa);
        em.getTransaction().commit();
        
    }
    
    public static void main(String[] args) {
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem2PU");
        em = emf.createEntityManager();
        
        JMSContext var_context = fabrika.createContext();
        JMSConsumer var_consumer = var_context.createConsumer(red1);
        JMSProducer var_producer = var_context.createProducer();
        
        context = var_context;
        consumer = var_consumer;
        producer = var_producer;
        
        while(true){
            try {   
//                while(true){
//                    zahtev = (TextMessage)consumer.receiveNoWait();
//                    if(zahtev != null) break;
//                }
                TextMessage zahtev = (TextMessage)consumer.receive();
                int vrsta = zahtev.getIntProperty("vrsta");
                
                switch(vrsta){
                    case 5:
                        kreiraj_kategoriju(zahtev);
                        break;
                    case 6:
                        kreiraj_artikal(zahtev);
                        break;
                    case 7:
                        menjaj_cenu_artikla(zahtev);
                        break;
                    case 8:
                        menjaj_popust_artikla(zahtev);
                        break;
                    case 9:
                        dodaj_u_korpu(zahtev);
                        break;
                    case 10:
                        brisi_iz_korpe(zahtev);
                        break;
                    case 14:
                        dohvati_sve_kategorije(zahtev);
                        break;
                    case 15:
                        dohvati_moje_artikle_koje_prodajem(zahtev);
                        break;
                    case 16:
                        dohvati_moje_artikle_iz_korpe(zahtev);
                        break;
                    case -4:
                        dohvati_korpu_sa_artiklima(zahtev);
                        break;
                }

            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
