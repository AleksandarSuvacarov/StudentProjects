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


@Path("transakcije")
public class Transakcije_rest {
    
    @Resource(lookup="mojaFabrika")
    private ConnectionFactory fabrika;
    
    @Resource(lookup="mojTopic1")
    private Topic topic1;
    
    @Resource(lookup="mojRed3")
    private Queue red1;
    
    @Resource(lookup="mojRed")
    private Queue red_korisnici;
    
    @Resource(lookup="mojRed2")
    private Queue red_artikli;
    
    @Resource(lookup="mojTopic2")
    private Topic topic2;
    
    @POST
    @Path("placanje/{adresa}/{grad}/{idKor}")
    public Response izvrsi_placanje(@PathParam("adresa") String adresa, @PathParam("grad") String grad, @PathParam("idKor") int idKor){
        
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
        
        TextMessage uslov2 = context1.createTextMessage();
        int idGrad = 0;
        try {
            uslov2.setStringProperty("naziv", grad);
            uslov2.setIntProperty("vrsta", -3);
        
            
            producer1.send(red_korisnici, uslov2);
            TextMessage txt = (TextMessage)consumer1.receive();
            
            if(txt.getIntProperty("found") == 0)
                return Response.status(txt.getIntProperty("kod")).entity(txt.getText()).build(); 
            
            idGrad = txt.getIntProperty("idGrad");
            
        } catch (JMSException ex) {
            Logger.getLogger(Artikli_rest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 11);
            txtMsg.setIntProperty("idGrad", idGrad);
            txtMsg.setIntProperty("idKor", idKor);
            txtMsg.setStringProperty("adresa", adresa);
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
    @Path("mojeNarudzbine")
    public Response dohvati_moje_narudzbine(@Context HttpHeaders headers){
        
        List<String> requestHeader = headers.getRequestHeader("idKor");
        int idKor = Integer.parseInt(requestHeader.get(0));
        
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 17);
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
    @Path("narudzbine")
    public Response dohvati_sve_narudzbine(){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 18);
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
    @Path("transakcije")
    public Response dohvati_sve_transakcije(){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 19);
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
