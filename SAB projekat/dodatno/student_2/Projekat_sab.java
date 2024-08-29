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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;

/**
 *
 * @author Aleksa
 */
public class Projekat_sab {

    /**
     * @param args the command line arguments
     */
    public static Connection conn = DB.getInstance().getConnection();
    
    public static void Dohvati(){
        try(PreparedStatement ps = conn.prepareStatement("select * from Vozilo")){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("RegBr"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Projekat_sab.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public sa200355_CityOperations cityOperations = new sa200355_CityOperations();
    public sa200355_DistrictOperations districtOperations = new sa200355_DistrictOperations();
    
    public void insertDistrict_ExistingCity() {
       int idCity = this.cityOperations.insertCity("Belgrade", "11000");
       Assert.assertNotEquals(-1L, (long)idCity);
       int idDistrict = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
       Assert.assertNotEquals(-1L, (long)idDistrict);
       boolean t = this.districtOperations.getAllDistrictsFromCity(idCity).contains(idDistrict);
       System.out.println(t);
       Assert.assertTrue(this.districtOperations.getAllDistrictsFromCity(idCity).contains(idDistrict));
    }
    
    public static void main(String[] args) {
        
        Projekat_sab klasa = new Projekat_sab();
        klasa.insertDistrict_ExistingCity();
    }
    
}
