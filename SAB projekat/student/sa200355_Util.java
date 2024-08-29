/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Aleksa
 */
public class sa200355_Util {
    
    public static Connection conn = DB.getInstance().getConnection();
    
    public static int utilGetUserId(String korime){
        String queryStr = "Select idK from Korisnik where korime=?";
        int idK = -1;
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setString(1, korime);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idK = rs.getInt(1);
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idK;
    }
    
    public static int utilGetVehicleId(String regBr){
        String queryStr = "Select idV from Vozilo where regBr=?";
        int idV = -1;
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setString(1, regBr);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idV = rs.getInt(1);
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idV;
    }
    
    public static boolean utilCityExists(int idG){
        String queryStr = "Select * from Grad where idG=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idG);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return true; //Postoji grad
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static int utilGetCityId(String naziv){
        int idG = -1;
        String queryStr = "Select * from Grad where naziv=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setString(1, naziv);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idG = rs.getInt(1);
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idG;
    }
    
    public static boolean utilDistrictExists(int idO){
        String queryStr = "Select * from Opstina where idO=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idO);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return true; //Postoji opstina
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean utilCourierExists(int idK){
        String queryStr = "Select * from Kurir where idK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return true; //Postoji kurir
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static int utilGetCourierStatus(int idK){
        String queryStr = "Select Status from Kurir where idK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt(1); //Dobijen status
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public static int utilGetCourierFuelType(int idK){
        String queryStr = "select TipGoriva\n" +
                            "from Kurir k join Vozilo v on(k.IdV=v.IdV)\n" +
                            "where k.IdK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt(1); //Dobijeno gorivo
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public static double utilGetCourierFuelConsumption(int idK){
        String queryStr = "select Potrosnja\n" +
                            "from Kurir k join Vozilo v on(k.IdV=v.IdV)\n" +
                            "where k.IdK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getDouble(1); //Dobijeno gorivo
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public static boolean utilSetCourierStatus(int idK, int status){
        String queryStr = "update Kurir set Status=? where idK=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, status);
            ps.setInt(2, idK);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true; //Postavljen status
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean utilPackageExists(int idP){
        String queryStr = "Select * from Paket where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idP);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return true; //Postoji paket
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static double utilEuclidean(int x1, int y1, int x2, int y2) {
      return Math.sqrt((double)((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }
    
    public static int utilGetDistrictX(int idO){
        String queryStr = "Select Xkoor from Opstina where idO=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idO);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt(1); //Postoji opstina i njena x koor
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }    
    
    public static int utilGetDistrictY(int idO){
        String queryStr = "Select Ykoor from Opstina where idO=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idO);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt(1); //Postoji opstina i njena y koor
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }   
    
    
    
    
}
