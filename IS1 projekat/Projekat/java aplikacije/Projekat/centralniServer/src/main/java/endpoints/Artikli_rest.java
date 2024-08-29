/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import javax.ws.rs.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import endpoints.Korisnici_rest;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;


@Path("artikli")
public class Artikli_rest {
    
    @Resource(lookup="mojaFabrika")
    private ConnectionFactory fabrika;
    
    @Resource(lookup="mojTopic1")
    private Topic topic1;
    
    @Resource(lookup="mojRed2")
    private Queue red1;
    
    @Resource(lookup="mojRed")
    private Queue red_korisnici;
    
    @Resource(lookup="mojTopic2")
    private Topic topic2;
    
    
    @POST
    @Path("kategorija/{naziv}")
    public Response kreiraj_kategoriju(@PathParam("naziv") String naziv){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 5);
            txtMsg.setStringProperty("naziv", naziv);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
        
        
        
    }
    
    @POST
    @Path("artikal/{naziv}/{opis}/{cena}/{popust}/{kategorija}/{idKor}")
    public Response kreiraj_artikal(@PathParam("naziv") String naziv, @PathParam("opis") String opis, @PathParam("cena") double cena,
    @PathParam("popust") double popust, @PathParam("kategorija") String kategorija, @PathParam("idKor") int idKor){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
 
        TextMessage uslov = context1.createTextMessage();
        try {
            uslov.setIntProperty("idKor", idKor);
            uslov.setIntProperty("vrsta", -1);
        
            producer1.send(red_korisnici, uslov);
            TextMessage txt = (TextMessage)consumer1.receive();
            
            if(txt.getIntProperty("found") == 0)
                return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build(); 
            
        } catch (JMSException ex) {
            Logger.getLogger(Artikli_rest.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 6);
            txtMsg.setStringProperty("naziv", naziv);
            txtMsg.setStringProperty("opis", opis);
            txtMsg.setDoubleProperty("cena", cena);
            txtMsg.setDoubleProperty("popust", popust / 100);
            txtMsg.setStringProperty("kategorija", kategorija);
            txtMsg.setIntProperty("idKor", idKor);
            
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
        
        
        
    }
    
    @PUT
    @Path("cena/{naziv}/{cena}")
    public Response menjanje_cene_artikla(@PathParam("naziv") String naziv, @PathParam("cena") double cena){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 7);
            txtMsg.setDoubleProperty("cena", cena);
            txtMsg.setStringProperty("naziv", naziv);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
        
        
        
    }
    
    @PUT
    @Path("popust/{naziv}/{popust}")
    public Response menjanje_popusta_artikla(@PathParam("naziv") String naziv, @PathParam("popust") double popust){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 8);
            txtMsg.setDoubleProperty("popust", popust/100);
            txtMsg.setStringProperty("naziv", naziv);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
        
        
        
    }
    
    @POST
    @Path("dodavanje/{idKor}/{artikal}/{kolicina}")
    public Response dodaj_u_korpu(@PathParam("idKor") int idKor, @PathParam("artikal") String artikal, @PathParam("kolicina") int kolicna){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
                
        TextMessage uslov = context1.createTextMessage();
        try {
            uslov.setIntProperty("idKor", idKor);
            uslov.setIntProperty("vrsta", -1);
            
            producer1.send(red_korisnici, uslov);
            TextMessage txt = (TextMessage)consumer1.receive();
            
            if(txt.getIntProperty("found") == 0)
                return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build(); 
            
        } catch (JMSException ex) {
            Logger.getLogger(Artikli_rest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 9);
            txtMsg.setIntProperty("idKor", idKor);
            txtMsg.setStringProperty("artikal", artikal);
            txtMsg.setIntProperty("kolicina", kolicna);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
                
    }
    
    @DELETE
    @Path("brisanje/{idKor}/{artikal}/{kolicina}")
    public Response brisi_iz_korpe(@PathParam("idKor") int idKor, @PathParam("artikal") String artikal, @PathParam("kolicina") int kolicna){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
                
        TextMessage uslov = context1.createTextMessage();
        try {
            uslov.setIntProperty("idKor", idKor);
            uslov.setIntProperty("vrsta", -1);
            
            producer1.send(red_korisnici, uslov);
            TextMessage txt = (TextMessage)consumer1.receive();
            
            if(txt.getIntProperty("found") == 0)
                return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build(); 
            
        } catch (JMSException ex) {
            Logger.getLogger(Artikli_rest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 10);
            txtMsg.setIntProperty("idKor", idKor);
            txtMsg.setStringProperty("artikal", artikal);
            txtMsg.setIntProperty("kolicina", kolicna);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
                
    }
    
    @GET
    @Path("kategorije")
    public Response dohvati_sve_kategorije(){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 14);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
        
        
        
    }
    
    @GET
    @Path("prodavanje")
    public Response dohvati_moje_artikle_koje_prodajem(@Context HttpHeaders headers){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        List<String> requestHeader = headers.getRequestHeader("idKor");
        int idKor = Integer.parseInt(requestHeader.get(0));
                
        
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 15);
            txtMsg.setIntProperty("idKor", idKor);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
        
        
        
    }
    
    @GET
    @Path("kupovina")
    public Response dohvati_moje_artikle_iz_korpe(@Context HttpHeaders headers){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        List<String> requestHeader = headers.getRequestHeader("idKor");
        int idKor = Integer.parseInt(requestHeader.get(0));
                
        
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 16);
            txtMsg.setIntProperty("idKor", idKor);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            
            return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
        
        
        
    }
}
