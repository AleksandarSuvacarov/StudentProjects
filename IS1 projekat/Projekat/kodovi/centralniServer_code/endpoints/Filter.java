/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Aleksa
 */
@Provider
public class Filter implements ContainerRequestFilter{
 
    @Resource(lookup="mojaFabrika")
    private ConnectionFactory fabrika;
    
//    @Resource(lookup="mojTopic1")
//    private Topic topic1;
    
    @Resource(lookup="mojRed2")
    private Queue red1;
    
    @Resource(lookup="mojRed")
    private Queue red_korisnici;
    
//    @Resource(lookup="mojTopic1")
//    private Topic red_korisnici;
    
    @Resource(lookup="mojTopic2")
    private Topic topic2;
    
    @Override
    public void filter(ContainerRequestContext arg) throws IOException {
       
        try {
            
            List<String> header = arg.getHeaders().get("Authorization");
            
            if(header == null || header.isEmpty()){
                Response res = Response.status(Response.Status.UNAUTHORIZED).entity("Posaljite kredencijale").build();
                arg.abortWith(res);
                return;
            }                
            
            
            String authHeaderValue = header.get(0);
            String[] decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")),StandardCharsets.UTF_8).split(":");
            String username = decodedAuthHeaderValue[0];
            String password = decodedAuthHeaderValue[1];
            
            
            JMSContext context1 = fabrika.createContext();
            JMSConsumer consumer1 = context1.createConsumer(topic2);
            JMSProducer producer1 = context1.createProducer();
            
            TextMessage uslov = context1.createTextMessage();
            TextMessage txt = null;

            uslov.setStringProperty("username", username);
            uslov.setIntProperty("vrsta", -2);

            producer1.send(red_korisnici, uslov);
            txt = (TextMessage)consumer1.receive();

            if(txt.getIntProperty("found") == 0){
                Response res = Response.status(Response.Status.UNAUTHORIZED).entity("Pogresan username").build();
                arg.abortWith(res);
                return;
            }

            
            if(!txt.getStringProperty("password").equals(password)){
                Response res = Response.status(Response.Status.UNAUTHORIZED).entity("Pogresan password").build();
                arg.abortWith(res);
                return;
            }
            
            Integer idK = txt.getIntProperty("idKor");
            arg.getHeaders().add("idKor", idK.toString());
            
            
        } catch (JMSException ex) {
            Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
