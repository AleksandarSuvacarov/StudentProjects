/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.*;
import java.time.*;
import java.util.Date;
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
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
public class Main {

    private static EntityManager em;
    
//    private static EntityManager em = null;
    
    @Resource(lookup="mojaFabrika")
    private static ConnectionFactory fabrika;
    
    @Resource(lookup="mojTopic1")
    private static Topic topic1;
    
    @Resource(lookup="mojRed3")
    private static Queue red1;
    
    @Resource(lookup="mojRed2")
    private static Queue red_artikli;
    
    @Resource(lookup="mojRed")
    private static Queue red_korisnici;
    
    @Resource(lookup="mojTopic2")
    private static Topic topic2;
    
    private static JMSContext context = null;
    private static JMSConsumer consumer = null;
    private static JMSProducer producer = null;
    
    private static void izvrsi_placanje(TextMessage zahtev) throws JMSException{
        int idGrad = zahtev.getIntProperty("idGrad");
        int idKor = zahtev.getIntProperty("idKor");
        String adresa = zahtev.getStringProperty("adresa");
        
        TextMessage uslov = context.createTextMessage();
        uslov.setIntProperty("vrsta", -4);
        uslov.setIntProperty("idKor", idKor);
        producer.send(red_artikli, uslov);
        
        TextMessage odgovor = (TextMessage)consumer.receive();
        if(odgovor.getIntProperty("found") == 0){
            TextMessage txt = context.createTextMessage("Korisnik nema korpu");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        
        
        
        Narudzbina nar = new Narudzbina();
        nar.setAdresa(adresa);
        nar.setCena(0);
        nar.setIdGrad(idGrad);
        nar.setIdKor(idKor);
        nar.setVreme(new Date());
        
        em.getTransaction().begin();
        em.persist(nar);
        em.getTransaction().commit();
        
        for(int i = 0; i < odgovor.getIntProperty("velicina"); i++){
            TextMessage artMsg = (TextMessage)consumer.receive();
            int idArt = artMsg.getIntProperty("idArt");
            int kolicina = artMsg.getIntProperty("kolicina");
            double cena = artMsg.getDoubleProperty("cena");
            int idArtKor = artMsg.getIntProperty("idKor");
            
            Stavka stavka = new Stavka();
            stavka.setIdArt(idArt);
            stavka.setCena(cena);
            stavka.setKolicina(kolicina);
            stavka.setIdNar(nar);
            
            em.getTransaction().begin();
            em.persist(stavka);
            nar.setCena(nar.getCena() + cena * kolicina);
            em.getTransaction().commit();
            
            
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("vrsta", 3);
            txtMsg.setIntProperty("idKor", idArtKor);
            txtMsg.setDoubleProperty("novac", cena * kolicina);
            txtMsg.setIntProperty("trebaOdgovor", 0);
            producer.send(red_korisnici, txtMsg);
        }
        
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setIntProperty("vrsta", 3);
        txtMsg.setIntProperty("idKor", idKor);
        txtMsg.setDoubleProperty("novac", (-1) * nar.getCena());
        txtMsg.setIntProperty("trebaOdgovor", 0);
        producer.send(red_korisnici, txtMsg);
        
        Transakcija tran = new Transakcija();
        tran.setIdNarTran(nar);
        tran.setSuma(nar.getCena());
        tran.setVreme(new Date());
        
        em.getTransaction().begin();
        em.persist(tran);
        em.getTransaction().commit();       
        
        
        
        TextMessage txt = context.createTextMessage("Uspesno obavljena transakcija");
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
    }
    
    private static void dohvati_moje_narudzbine(TextMessage zahtev) throws JMSException  {
        int idKor = zahtev.getIntProperty("idKor");
        
        List<Narudzbina> rl = em.createNamedQuery("Narudzbina.findByIdKor", Narudzbina.class).setParameter("idKor", idKor).getResultList();
                
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Korisnik nema ni jednu narudzbinu");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }
        
        Narudzbina nar = rl.get(0);
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setIntProperty("vrsta", -5);
        txtMsg.setIntProperty("idGrad", nar.getIdGrad());
        producer.send(red_korisnici, txtMsg);
        
        TextMessage odg = (TextMessage)consumer.receive();
        if(odg.getIntProperty("found") == 0){
            TextMessage txt = context.createTextMessage("Greska");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        String naziv_grada = odg.getStringProperty("naziv");
        String str = nar.getIdNar().toString() + "-" + nar.getCena() + "-" + nar.getVreme().toString().split(" ")[3] + 
                "-" + nar.getAdresa() + "-" + naziv_grada + "-" + nar.getIdKor();       
        for(int i = 1; i < rl.size(); i++){
            nar = rl.get(i);
            txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("vrsta", -5);
            txtMsg.setIntProperty("idGrad", nar.getIdGrad());
            producer.send(red_korisnici, txtMsg);
            
            odg = (TextMessage)consumer.receive();
            if(odg.getIntProperty("found") == 0){
            TextMessage txt = context.createTextMessage("Greska");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
            }
            naziv_grada = odg.getStringProperty("naziv");
            str += "\n" + nar.getIdNar().toString() + "-" + nar.getCena() + "-" + nar.getVreme().toString().split(" ")[3] + 
                "-" + nar.getAdresa() + "-" + naziv_grada + "-" + nar.getIdKor(); 
        }
            
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
        
    }
    
    private static void dohvati_sve_narudzbine(TextMessage zahtev) throws JMSException  {
        List<Narudzbina> rl = em.createNamedQuery("Narudzbina.findAll", Narudzbina.class).getResultList();
                
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Nema narudzbina");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }
        Narudzbina nar = rl.get(0);
        
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setIntProperty("vrsta", -5);
        txtMsg.setIntProperty("idGrad", nar.getIdGrad());
        producer.send(red_korisnici, txtMsg);
        
        TextMessage odg = (TextMessage)consumer.receive();
        if(odg.getIntProperty("found") == 0){
            TextMessage txt = context.createTextMessage("Greska");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
        }
        String naziv_grada = odg.getStringProperty("naziv");
        
        String str = nar.getIdNar().toString() + "-" + nar.getCena() + "-" + nar.getVreme().toString().split(" ")[3] + 
                "-" + nar.getAdresa() + "-" + naziv_grada + "-" + nar.getIdKor();       
        for(int i = 1; i < rl.size(); i++){
            nar = rl.get(i);
            
            txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("vrsta", -5);
            txtMsg.setIntProperty("idGrad", nar.getIdGrad());
            producer.send(red_korisnici, txtMsg);
            
            odg = (TextMessage)consumer.receive();
            if(odg.getIntProperty("found") == 0){
            TextMessage txt = context.createTextMessage("Greska");
            txt.setIntProperty("kod", 400);
            producer.send(topic2, txt);
            return;
            }
            naziv_grada = odg.getStringProperty("naziv");
            
            str += "\n" + nar.getIdNar().toString() + "-" + nar.getCena() + "-" + nar.getVreme().toString().split(" ")[3] + 
                "-" + nar.getAdresa() + "-" + naziv_grada + "-" + nar.getIdKor(); 
        }
            
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
        
    }
    
    private static void dohvati_sve_transakcije(TextMessage zahtev) throws JMSException  {
        List<Transakcija> rl = em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();
                
        if(rl == null || rl.isEmpty()){
            TextMessage txt = context.createTextMessage("Nema transakcija");
            txt.setIntProperty("kod", 200);
            producer.send(topic2, txt);
            return;
        }
        Transakcija tran = rl.get(0);
        String str =  tran.getIdTran() + "-" + tran.getIdNarTran().getIdNar() + "-" + tran.getSuma() + "-" + tran.getVreme().toString().split(" ")[3];
        for(int i = 1; i < rl.size(); i++){
            tran = rl.get(i);
            str += "\n" + tran.getIdTran() + "-" + tran.getIdNarTran().getIdNar() + "-" + tran.getSuma() + "-" + tran.getVreme().toString().split(" ")[3];
        }
            
        TextMessage txt = context.createTextMessage(str);
        txt.setIntProperty("kod", 200);
        producer.send(topic2, txt);
        
        
    }
    
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem3PU");
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
                    case 11:
                        izvrsi_placanje(zahtev);
                        break;
                    case 17:
                        dohvati_moje_narudzbine(zahtev);
                        break;
                    case 18:
                        dohvati_sve_narudzbine(zahtev);
                        break;
                    case 19:
                        dohvati_sve_transakcije(zahtev);
                        break;
                }

            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
 
        
    }
    
}
