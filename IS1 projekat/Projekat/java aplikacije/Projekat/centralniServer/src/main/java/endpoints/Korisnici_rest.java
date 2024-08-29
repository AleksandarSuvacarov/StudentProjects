/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

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


/**
 *
 * @author Aleksa
 */
@Path("korisnici")
public class Korisnici_rest {
    
    
    @Resource(lookup="mojaFabrika")
    private ConnectionFactory fabrika;
    
//    @Resource(lookup="mojTopic1")
//    private Topic topic1;
    
    @Resource(lookup="mojRed")
    private Queue red1;
    
//    @Resource(lookup="mojTopic1")
//    private Topic red1;
    
    @Resource(lookup="mojTopic2")
    private Topic topic2;
    
    
    @GET
    @Path("{naziv}")
    public Response dohvati_grad(@PathParam("naziv") String naziv){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", -3);
            txtMsg.setStringProperty("naziv", naziv);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            return Response.status(Response.Status.OK).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
    }
    
    @POST
    @Path("grad/{naziv}")
    public Response kreiraj_grad(@PathParam("naziv") String naziv){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 1);
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
    @Path("korisnik/{ime}/{prezime}/{username}/{password}/{adresa}/{grad}/{novac}")
    public Response kreiraj_korisnika(@PathParam("ime") String ime, @PathParam("prezime") String prezime, @PathParam("username") String username,
    @PathParam("password") String password, @PathParam("adresa") String adresa, @PathParam("grad") String grad, @PathParam("novac") double novac){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 2);
            txtMsg.setStringProperty("ime", ime);
            txtMsg.setStringProperty("prezime", prezime);
            txtMsg.setStringProperty("username", username);
            txtMsg.setStringProperty("password", password);
            txtMsg.setStringProperty("adresa", adresa);
            txtMsg.setStringProperty("grad", grad);
            txtMsg.setDoubleProperty("novac", novac);
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
    @Path("novac/{idKor}/{novac}")
    public Response dodavanje_novca_korisniku(@PathParam("idKor") int idKor, @PathParam("novac") double novac){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 3);
            txtMsg.setIntProperty("idKor", idKor);
            txtMsg.setDoubleProperty("novac", novac);
            txtMsg.setIntProperty("trebaOdgovor", 1);
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
    @Path("stanovanje/{idK}/{adresa}/{grad}")
    public Response promena_adrese_i_grada_korisniku(@PathParam("idK") int idK, @PathParam("adresa") String adresa, @PathParam("grad") String grad){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 4);
            txtMsg.setIntProperty("idK", idK);
            txtMsg.setStringProperty("adresa", adresa);
            txtMsg.setStringProperty("grad", grad);
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
    @Path("gradovi")
    public Response dohvati_sve_gradove(){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 12);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            return Response.status(Response.Status.OK).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
    }
    
    @GET
    @Path("korisnici")
    public Response dohvati_sve_korisnike(){
        
        JMSContext context1 = fabrika.createContext();
        JMSConsumer consumer1 = context1.createConsumer(topic2);
        JMSProducer producer1 = context1.createProducer();
        
        try {
            TextMessage txtMsg = context1.createTextMessage();
            txtMsg.setIntProperty("vrsta", 13);
            producer1.send(red1, txtMsg);
            
            TextMessage txt = (TextMessage)consumer1.receive();
            
            consumer1.close();
            context1.close();
            
            return Response.status(Response.Status.OK).entity(txt.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici_rest.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Bacena greska vrv").build();
    }
}
