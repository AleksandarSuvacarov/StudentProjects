/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entiteti.Grad;
import entiteti.Korisnik;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

/**
 *
 * @author Aleksa
 */
public class Main {
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
//    private static EntityManager em = null;
    
    @Resource(lookup="mojaFabrika")
    private static ConnectionFactory fabrika;
    
//    @Resource(lookup="mojTopic1")
//    private static Topic topic1;
    
    @Resource(lookup="mojRed")
    private static Queue red1;
    
    @Resource(lookup="mojRed3")
    private static Queue red_transakcije;
    
//    @Resource(lookup="mojTopic1")
//    private static Topic red1;
    
    @Resource(lookup="mojTopic2")
    private static Topic topic2;
    
    private static JMSContext context = null;
    private static JMSConsumer consumer = null;
    private static JMSProducer producer = null;
    
//    private static TextMessage zahtev = null;
//    private static int a = 0;
    
    private static void dohvati_grad(TextMessage zahtev) throws JMSException{
        String naziv = zahtev.getStringProperty("naziv");

        TypedQuery<Grad> nq = em.createNamedQuery("Grad.findByNaziv", Grad.class);
        nq.setParameter("naziv", naziv);
        List<Grad> r = nq.getResultList();
        
        if(r == null || r.isEmpty()){
            TextMessage txt = context.createTextMessage("Nema grada");
            txt.setIntProperty("kod", 400);
            txt.setIntProperty("found", 0);
            producer.send(topic2, txt);
            return;
        }
        Grad g = r.get(0);
        String str = g.getIdGrad().toString() + "-" + g.getNaziv();
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        txt.setIntProperty("found", 1);
        txt.setIntProperty("idGrad", g.getIdGrad());
        producer.send(topic2, txt);
    }
    
    private static void kreiraj_grad(TextMessage zahtev) throws JMSException{

        String naziv = zahtev.getStringProperty("naziv");

        List<Grad> rl = em.createQuery("SELECT g FROM Grad g WHERE g.naziv = :naziv", Grad.class).setParameter("naziv", naziv).getResultList();
        if(rl != null && !rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Grad vec postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }

        Grad g = new Grad();
        g.setNaziv(naziv);
        em.getTransaction().begin();
        em.persist(g);
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno dodat grad");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);

    }
    
    private static void kreiraj_korisnika(TextMessage zahtev) throws JMSException{
        String ime = zahtev.getStringProperty("ime");
        String prezime = zahtev.getStringProperty("prezime");
        String username = zahtev.getStringProperty("username");
        String password = zahtev.getStringProperty("password");
        String adresa = zahtev.getStringProperty("adresa");
        String grad = zahtev.getStringProperty("grad");
        double novac = zahtev.getDoubleProperty("novac");

        List<Grad> rl = em.createQuery("SELECT g FROM Grad g WHERE g.naziv = :naziv", Grad.class).setParameter("naziv", grad).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Grad ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }

        Korisnik k = new Korisnik();
       

        k.setAdresa(adresa);
        k.setIme(ime);
        k.setPrezime(prezime);
        k.setKorisnickoIme(username);
        k.setSifra(password);
        k.setNovac(novac);
        k.setIdGrad(rl.get(0));
        
        
        em.getTransaction().begin();
        em.persist(k);
        em.getTransaction().commit();
        

        
        TextMessage txt = context.createTextMessage("Uspesno dodat korisnik");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
    }
    
    private static void dodavanje_novca_korisniku(TextMessage zahtev) throws JMSException{

        int idK = zahtev.getIntProperty("idKor");
        double novac = zahtev.getDoubleProperty("novac");

        Korisnik k = em.find(Korisnik.class, idK);
        if(k == null){
            TextMessage txt = context.createTextMessage("Korisnik ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }

        
        em.getTransaction().begin();
        k.setNovac(k.getNovac() + novac);
        em.getTransaction().commit();

        if(zahtev.getIntProperty("trebaOdgovor") == 0)
            return;
        
        TextMessage txt = context.createTextMessage("Uspesno uvecan novac");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);

    }
    
    private static void promena_adrese_i_grada_korisniku(TextMessage zahtev) throws JMSException{

        int idK = zahtev.getIntProperty("idK");
        String adresa = zahtev.getStringProperty("adresa");
        String grad = zahtev.getStringProperty("grad");
        
        Korisnik k = em.find(Korisnik.class, idK);
        if(k == null){
            TextMessage txt = context.createTextMessage("Korisnik ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        
        List<Grad> rl = em.createQuery("SELECT g FROM Grad g WHERE g.naziv = :naziv", Grad.class).setParameter("naziv", grad).getResultList();
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Grad ne postoji");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        Grad g = rl.get(0);
        
        
        em.getTransaction().begin();
        k.setAdresa(adresa);
        k.setIdGrad(g);
        em.getTransaction().commit();

        TextMessage txt = context.createTextMessage("Uspesno promenjeno mesto stanovanja");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);

    }
    
    private static void dohvati_sve_gradove(TextMessage zahtev) throws JMSException{

        List<Grad> rl = em.createNamedQuery("Grad.findAll", Grad.class).getResultList();
                
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Nema gradova");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }
        String str = rl.get(0).getIdGrad().toString() + "-" + rl.get(0).getNaziv();
        for(int i = 1; i < rl.size(); i++){
            str = str + "\n" +rl.get(i).getIdGrad().toString() + "-" + rl.get(i).getNaziv();
        }
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
    }
    
    private static void dohvati_sve_korisnike(TextMessage zahtev) throws JMSException{

        List<Korisnik> rl = em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
                
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Nema korisnika");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }
        String str = rl.get(0).getIdKor().toString() + "-" + rl.get(0).getIme()+ "-" + rl.get(0).getPrezime() + "-" + rl.get(0).getAdresa() + "-" + rl.get(0).getIdGrad().getNaziv() + "-" + rl.get(0).getNovac();
        for(int i = 1; i < rl.size(); i++){
            str = str + "\n" + rl.get(i).getIdKor().toString() + "-" + rl.get(i).getIme()+ "-" + rl.get(i).getPrezime() + "-" + rl.get(i).getAdresa() + "-" + rl.get(i).getIdGrad().getNaziv() + "-" + rl.get(i).getNovac();
        }
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
    }
    
    private static void dohvati_korisnika(TextMessage zahtev)throws JMSException{
        int idKor = zahtev.getIntProperty("idKor");

        Korisnik kor = em.find(Korisnik.class, idKor);
        
        if(kor == null){
            TextMessage txt = context.createTextMessage("Nema korisnika");
            txt.setIntProperty("found", 0);
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        
        TextMessage txt = context.createTextMessage("Pronadjen korisnik");
        txt.setIntProperty("kod", 200);
        txt.setIntProperty("found", 1);
        producer.send(topic2, txt);
    }
    
    private static void dohvati_korisnika_username(TextMessage zahtev)throws JMSException{
        String username = zahtev.getStringProperty("username");

        List<Korisnik> rl = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :username", Korisnik.class).setParameter("username", username).getResultList();
        
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Nema korisnika");
            txt.setIntProperty("found", 0);
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        Korisnik kor = rl.get(0);
        TextMessage txt = context.createTextMessage("Pronadjen korisnik");
        txt.setIntProperty("kod", 200);
        txt.setIntProperty("found", 1);
        txt.setStringProperty("username", kor.getKorisnickoIme());
        txt.setStringProperty("password", kor.getSifra());
        txt.setIntProperty("idKor", kor.getIdKor());
        producer.send(topic2, txt);
    }
    
    private static void dohvati_grad_za_narudzbine(TextMessage zahtev) throws JMSException{
        int idGrad = zahtev.getIntProperty("idGrad");

        Grad g = em.find(Grad.class, idGrad);       
        
        if(g == null){
            TextMessage txt = context.createTextMessage("Nema grada");
            txt.setIntProperty("kod", 400);
            txt.setIntProperty("found", 0);
            producer.send(red_transakcije, txt);
            return;
        }
        TextMessage txt = context.createTextMessage();
        txt.setIntProperty("kod", 200);
        txt.setIntProperty("found", 1);
        txt.setStringProperty("naziv", g.getNaziv());
        producer.send(red_transakcije, txt);
    }
    
    public static void main(String[] args) {
        
        emf = Persistence.createEntityManagerFactory("podsistem1PU");        
        em = emf.createEntityManager();
        
        JMSContext var_context = fabrika.createContext();
        JMSConsumer var_consumer = var_context.createConsumer(red1);
        JMSProducer var_producer = var_context.createProducer();
        
        context = var_context;
        consumer = var_consumer;
        producer = var_producer;
        
        

//        TextMessage zahtev = null;
        while(true){
            try {   
//                while(true){
//                    zahtev = (TextMessage)consumer.receiveNoWait();
//                    if(zahtev != null) break;
//                }

                TextMessage zahtev = (TextMessage)consumer.receive();
                int vrsta = zahtev.getIntProperty("vrsta");
                
                switch(vrsta){
                    case 1:
                        kreiraj_grad(zahtev);
                        break;
                    case 2:
                        kreiraj_korisnika(zahtev);
                        break;
                    case 3:
                        dodavanje_novca_korisniku(zahtev);
                        break;
                    case 4:
                        promena_adrese_i_grada_korisniku(zahtev);
                        break;
                    case 12:
                        dohvati_sve_gradove(zahtev);
                        break;
                    case 13:
                        dohvati_sve_korisnike(zahtev);
                        break;
                    case -1:
                        dohvati_korisnika(zahtev);
                        break;
                    case -2:
                        dohvati_korisnika_username(zahtev);
                        break;
                    case -3:
                        dohvati_grad(zahtev);
                        break;
                    case -5:
                        dohvati_grad_za_narudzbine(zahtev);
                        break;
                }

            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        
        
    }  
}