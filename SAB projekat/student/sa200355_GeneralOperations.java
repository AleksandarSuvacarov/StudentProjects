
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author Aleksa
 */
public class sa200355_GeneralOperations implements GeneralOperations{

    public static Connection conn = DB.getInstance().getConnection();
    
    @Override
    public void eraseAll() {
        String queryStr = "delete from AktuelnaVoznja\n" +
                            "delete from Administrator\n" +
                            "delete from Ponuda\n" +
                            "delete from Paket\n" +
                            "delete from Kurir\n" +
                            "delete from Zahtev\n" +
                            "delete from Opstina\n" +
                            "delete from Korisnik\n" +
                            "delete from Vozilo\n" +
                            "delete from Grad";
        
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.executeUpdate();
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo celokupno brisanje");
        }
        
    }
    
}
