package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tasha
 */
public class DB {
    private static final String username = "sa";
    private static final String password = "etf123";
    private static final String database = "TransportPaketa";
    private static final int port = 1433;
    private static final String serverName = "localhost";
    
    //jdbc:sqlserver://[serverName[\instanceName][:portNumber]][;property=value[;property=value]]
    private static final String connectionString="jdbc:sqlserver://"
            + serverName + ":" + port + ";"
            + "encrypt=false;"
            + "database=" + database + ";"
            +"user=" + username 
            + ";password=" + password;
    
    private Connection connection;  
    
    private DB(){
        try {
            connection=DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            connection = null;
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
     
    private static DB db=null;
    
    public static DB getInstance()
    {
        if(db == null)
            db = new DB();
        return db;
    } 
}


