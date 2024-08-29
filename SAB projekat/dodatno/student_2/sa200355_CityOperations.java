/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author Aleksa
 */
public class sa200355_CityOperations implements CityOperations{
    
    public static Connection conn = DB.getInstance().getConnection();

    @Override
    public int insertCity(String naziv, String postanskiBr) {
        String queryStr = "insert into Grad(Naziv, PostanskiBr) values(?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, naziv);
            ps.setString(2, postanskiBr);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                //System.out.println(rs.getInt(1));
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CityOperations.class.getName()).log(Level.SEVERE, null, ex); 
            //System.out.println("Neuspelo dodavanje grada");
        }
        return -1;
    }

    @Override
    public int deleteCity(String... nazivi) {
        int brObrisanih = 0;
        for(String naziv : nazivi){
            String queryStr = "select idG from Grad where Naziv=?";
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setString(1, naziv);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    boolean t = deleteCity(rs.getInt(1));
                    if(t) brObrisanih++;
                }
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_CityOperations.class.getName()).log(Level.SEVERE, null, ex); 
                System.out.println("Neuspelo brisanje vise gradova po imenu");
            }
        }
        return brObrisanih;
    }

    @Override
    public boolean deleteCity(int idG) {
        String queryStr = "delete from Grad where idG=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idG);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CityOperations.class.getName()).log(Level.SEVERE, null, ex); 
            System.out.println("Neuspelo brisanje grada po idG");
        }
        return false;
    }

    @Override
    public List<Integer> getAllCities() {
        String queryStr = "select * from Grad";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<Integer> gradovi = new ArrayList<>();
            while(rs.next()){
                int idG = rs.getInt(1);
                gradovi.add(idG);
            }
            return gradovi;
//            if(gradovi.isEmpty()) return null;
//            else return gradovi;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih gradova");              
        }
        return null;
    }
    
    public void insertCity_OnlyOne() throws Exception {
      String name = "Tokyo";
      String postalCode = "100";
      int rowId = insertCity(name, postalCode);
      List<Integer> list = getAllCities();
      if(1L == (long)list.size()) System.out.println("Prosao 1");
      if(list.contains(rowId)) System.out.println("Prosao 2");
   }
            
    
    public static void main(String[] args) {
        sa200355_CityOperations klasa = new sa200355_CityOperations();
        int a = klasa.insertCity("Valjevo", "13");
        List<Integer> b = klasa.getAllCities();
        for (Integer t : b) {
            System.out.println(t);
        }
        
    }
    
}
