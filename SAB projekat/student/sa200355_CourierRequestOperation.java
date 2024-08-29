/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import rs.etf.sab.operations.CourierRequestOperation;
import static student.sa200355_Util.*;


/**
 *
 * @author Aleksa
 */
public class sa200355_CourierRequestOperation implements CourierRequestOperation{

    public static Connection conn = DB.getInstance().getConnection();
    
    @Override
    public boolean insertCourierRequest(String korime, String regBr) {
        int idK = utilGetUserId(korime);
        int idV = utilGetVehicleId(regBr);
        if(idK == -1) return false; //Nepostojeci korisnik
        if(idV == -1) return false; //Nepostojece vozilo
        
        String queryStr = "Select * from Kurir where idK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false; //Korisnik je vec kurir
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryStr = "Select * from Kurir where idV=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idV);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false; //Vozilo je zauzeto
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryStr = "insert into Zahtev(IdK, idV) values(?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ps.setInt(2, idV);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true; //Usepsno postavljen zahtev za kurirom
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            return false; // Zahtev za ovog korisnika vec postavljen
        }
        
        return false;
    }

    @Override
    public boolean deleteCourierRequest(String korime) {
        int idK = utilGetUserId(korime);
        if(idK == -1) return false; //Nepostojeci korisnik
        String queryStr = "delete from Zahtev where idK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true; //Usepsno postavljen zahtev za kurirom
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            return false; // Neuspelo brisanje zahteva
        }
        return false;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String korime, String regBr) {
        int idK = utilGetUserId(korime);
        int idV = utilGetVehicleId(regBr);
        if(idK == -1) return false; //Nepostojeci korisnik
        if(idV == -1) return false; //Nepostojece vozilo
                
        String queryStr = "Select * from Zahtev where idK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
                return false; // Nema zahteva za ovog korisnika
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryStr = "Select * from Kurir where idV=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idV);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false; //Vozilo je zauzeto
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryStr = "update Zahtev set IdV=? where IdK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idV);
            ps.setInt(2, idK);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true; //Usepsno azurirano vozilo u zahtevu
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            return false; // Nije azurirano vozilo u zahtevu
        }
        
        return false;
    }

    @Override
    public List<String> getAllCourierRequests() {
        String queryStr = "select z.IdK, k.Korime\n" +
                            "from Zahtev z join korisnik k on(z.idK=k.idK)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<String> kuriri_zahtevi = new ArrayList<>();
            while(rs.next()){
                String korime = rs.getString("Korime");
                kuriri_zahtevi.add(korime);
            }
            return kuriri_zahtevi;
//            if(kuriri_zahtevi.isEmpty()) return null;
//            else return kuriri_zahtevi;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih zahteva za kuririma");              
        }
        return null;
    }

    @Override
    public boolean grantRequest(String korime) {
        String queryStr = "{call spOdobriZahtev(?, ?)}";
        int povratni = 0;
        try(CallableStatement cs = conn.prepareCall(queryStr)){
            cs.setString(1, korime);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();
            povratni = cs.getInt(2);
            if(povratni == 0)
                return false; //Nije odobren zahtev
            else 
                return true; //Odobren zahtev
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo izvrsavanje odobravanja zahteva za kurire - grantRequest");
        }
        return false;
    }
    
    public static void main(String[] args) {
        sa200355_CourierRequestOperation klasa = new sa200355_CourierRequestOperation();
        List<String> lista = klasa.getAllCourierRequests();
        for(String s : lista){
            System.out.println(s);
        }
        boolean a = klasa.insertCourierRequest("m", "qwe");
        System.out.println(a);
        
        a = klasa.grantRequest("a");
        System.out.println(a);
        
    }
    
}
