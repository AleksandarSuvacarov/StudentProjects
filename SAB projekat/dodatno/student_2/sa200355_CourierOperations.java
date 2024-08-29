
package student;

import java.sql.Types;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.math.BigDecimal;
import rs.etf.sab.operations.CourierOperations;
import static student.sa200355_Util.*;

/**
 *
 * @author Aleksa
 */
public class sa200355_CourierOperations implements CourierOperations{
    
    public static Connection conn = DB.getInstance().getConnection();

    @Override
    public boolean insertCourier(String korime, String regBr) {
        int idK = utilGetUserId(korime);
        if (idK == -1) return false; //Nepostojeci korisnik
        int idV = utilGetVehicleId(regBr);
        if (idV == -1) return false; //Nepostojece vozilo
        
        String queryStr = "select * from Kurir where idK=? or idV=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ps.setInt(2, idV);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false;   //Nesto vec postoji u tabeli - ili idK ili idV, a to ne sme
            
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo ubacivanje kurira direktno 1 - insertCourier");
        }
        
        
        queryStr = "insert into Kurir(IdK, IdV) values(?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ps.setInt(2, idV);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;   //Uspesno dodat kurir
            
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo ubacivanje kurira direktno 2 - insertCourier");
        }
        return false;
    }

    @Override
    public boolean deleteCourier(String korime) {
        int idK = utilGetUserId(korime);
        if (idK == -1) return false; //Nepostojeci korisnik
        
        String queryStr = "delete from Kurir where idK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true; //Obrisan kurir
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neupselo brisanje kurira - deleteCourier");
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int status) {
        String queryStr = "select ko.korime\n" +
                            "from Kurir ku join Korisnik ko on(ku.idK=ko.idK)\n" +
                            "where ku.Status=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            List<String> kuriri = new ArrayList<>();
            String korime;
            while(rs.next()){
                korime = rs.getString(1);
                kuriri.add(korime);
            }
            return kuriri;
//            if(kuriri.isEmpty()) return null;
//            else return kuriri;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neupselo dohvatanje kurira sa statusom - getCouriersWithStatus");
        }
        return null;
    }

    @Override
    public List<String> getAllCouriers() {
        String queryStr = "select ko.korime \n" +
                            "from Kurir ku join Korisnik ko on(ku.idK=ko.idK)\n" +
                            "order by ku.Profit desc";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<String> kuriri = new ArrayList<>();
            String korime;
            while(rs.next()){
                korime = rs.getString(1);
                kuriri.add(korime);
            }
            return kuriri;
//            if(kuriri.isEmpty()) return null;
//            else return kuriri;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neupselo dohvatanje svih kurira - getAllCouriers");
        }
        return null;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int minBrIsporuka) {
        String queryStr = "select avg(Profit)\n" +
                            "from Kurir\n" +
                            "where BrIsporuka >= ?";
        double avg;
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, minBrIsporuka);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                avg = rs.getDouble(1);
                return new BigDecimal(avg);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neupselo dohvatanje average profita kurira");
        }
        return null;
        
    }
    
    public static void main(String[] args) {
        sa200355_CourierOperations klasa = new sa200355_CourierOperations();
        
//        boolean a = klasa.insertCourier("a", "q");
//        System.out.println(a);
        
        List<String> lista = klasa.getCouriersWithStatus(0);
        for(String s : lista){
            System.out.println(s);
        }
        
        BigDecimal bd = klasa.getAverageCourierProfit(0);
        System.out.println(bd);
    }
}
