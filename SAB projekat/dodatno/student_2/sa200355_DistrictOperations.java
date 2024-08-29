
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

import rs.etf.sab.operations.DistrictOperations;
import static student.sa200355_Util.*;

/**
 *
 * @author Aleksa
 */
public class sa200355_DistrictOperations implements DistrictOperations{
    
    public static Connection conn = DB.getInstance().getConnection();

    @Override
    public int insertDistrict(String naziv, int idG, int x, int y) {
        if(!utilCityExists(idG)) return -1;
        
        String queryStr = "select * from Opstina where idG=? and naziv=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idG);
            ps.setString(2, naziv);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return -1;  //Ovakva kombinacija vec postoji
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        queryStr = "insert into Opstina(idG, Naziv, Xkoor, Ykoor) values(?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, idG);
            ps.setString(2, naziv);
            ps.setInt(3, x);
            ps.setInt(4, y);
            int rowsAffected = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rowsAffected != 0 && rs.next())
                return rs.getInt(1);  //Uspesno dodata opstina
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public int deleteDistricts(String... nazivi) {
        int brObrisanih = 0;
        for(String naziv : nazivi){
            String queryStr = "select idO from Opstina where Naziv=?";
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setString(1, naziv);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    boolean t = deleteDistrict(rs.getInt(1));
                    if(t) brObrisanih++;
                }
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex); 
                System.out.println("Neuspelo brisanje vise opstina po imenu");
            }
        }
        return brObrisanih;
    }

    @Override
    public boolean deleteDistrict(int idO) {
        String queryStr = "delete from Opstina where idO=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idO);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex); 
            System.out.println("Neuspelo brisanje opstine po idO");
        }
        return false;
    }

    @Override
    public int deleteAllDistrictsFromCity(String nazivGrada) {
        int idG = utilGetCityId(nazivGrada);
        if(idG == -1) return -1;    //Nepostojeci grad
        String queryStr = "delete from Opstina where idG=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idG);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return rowsAffected;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex); 
            System.out.println("Neuspelo brisanje svih opstina iz jednog grada");
        }
        return 0;   //Nije bilo ni jedne opstine
        
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int idG) {
        if(!utilCityExists(idG)) 
            return null; // Grad ne postoji
        
        String queryStr = "select * from Opstina where idG=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idG);
            ResultSet rs = ps.executeQuery();
            List<Integer> opstine = new ArrayList<>();
            while(rs.next()){
                int idO = rs.getInt(1);
                opstine.add(idO);
            }
            if(opstine.isEmpty()) return null;
            else return opstine;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih opstina jednog grada");              
        }
        return null;
    }

    @Override
    public List<Integer> getAllDistricts() {
        String queryStr = "select * from Opstina";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<Integer> opstine = new ArrayList<>();
            while(rs.next()){
                int idO = rs.getInt(1);
                opstine.add(idO);
            }
            return opstine;
//            if(gradovi.isEmpty()) return null;
//            else return gradovi;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih opstina");              
        }
        return null;
    }
    
    public static void main(String[] args) {
        sa200355_DistrictOperations klasa = new sa200355_DistrictOperations();
        klasa.insertDistrict("Novi Beograd", 15, 5, 5);
        klasa.insertDistrict("Vozdovac", 15, 5, 5);
        klasa.insertDistrict("Stari grad", 15, 5, 5);
        klasa.insertDistrict("Medijana", 16, 5, 5);
        klasa.insertDistrict("Medijana", 17, 5, 5);
        klasa.insertDistrict("Palilula", 17, 5, 5);
        
        
        List<Integer> lista = klasa.getAllDistricts();
        for(int i : lista){
            System.out.println(i);
        }
        
        System.out.println("Sve osptine Beograda");
        lista = klasa.getAllDistrictsFromCity(15);
        for(int i : lista){
            System.out.println(i);
        }
        
        int a = klasa.deleteDistricts(new String[]{"Medijana", "Vzdvc"});
        System.out.println("Broj obrisanih opstina " + a);
        
    }
    
}
