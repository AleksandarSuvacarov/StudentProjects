/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jdbc_trznicentar;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdbc_trznicentar.DB;

/**
 *
 * @author Aleksa
 */
public class JDBC_TrzniCentar {

    public static void ispisiRadnike(){
        Connection conn = DB.getInstance().getConnection();
       
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Radnik");){
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()){
                
                for(int i = 1; i <= rsmd.getColumnCount(); i++){
                    if(rsmd.getColumnType(i) == java.sql.Types.INTEGER){
                        System.out.print(rsmd.getColumnName(i) + "=" + rs.getInt(i));
                    }
                    else{
                        System.out.print(" " + rsmd.getColumnName(i) + "=" + rs.getString(i));
                    }
                    
                }
                System.out.println();
                //System.out.println(rs.getString(2) + " " + rs.getString("Prezime"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBC_TrzniCentar.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
    }
    
    public static void izmeniInfoORadnicima(){
        Connection conn = DB.getInstance().getConnection();
       
        try (PreparedStatement ps = conn.prepareStatement("select * from Radnik ", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();){
            
            while(rs.next()){
                System.out.println(rs.getString(2) + " " + rs.getString("Prezime"));
                rs.updateString(2, rs.getString(2).substring(0, 17) +" ");
                rs.updateRow();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBC_TrzniCentar.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
    }
    
    public static void izmeniInfoORadnicimaProdavcima(){
        Connection conn = DB.getInstance().getConnection();
       
        try (PreparedStatement ps = conn.prepareStatement("select * from Radnik join Prodavac on Radnik.BrLK=Prodavac.BrLK", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();){
            
            while(rs.next()){
                System.out.println(rs.getString(2) + " " + rs.getString("Prezime"));
                rs.updateString(2, rs.getString(2).substring(0, 17) +"a");
                rs.updateRow();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBC_TrzniCentar.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
    }
    
    public static void dodajRadnika(String ime, String prezime, String BrTel, int SifA, int BrLK){
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into Radnik (Ime, Prezime, BrTel, SifA, BrLK) values (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setString(1, ime);
            ps.setString(2, prezime);
            ps.setString(3, BrTel);
            ps.setInt(4, SifA);
            ps.setInt(5, BrLK);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            
            if(rs.next()){
                System.out.println(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBC_TrzniCentar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int brRadnikaSaImenom(String ime) {
        Connection conn = DB.getInstance().getConnection();
        String query = "{ call SPBrojRadnikaSaImenom (?,?) }";
        try ( CallableStatement cs = conn.prepareCall(query)) {
            cs.setString(1, ime);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt(2);
        } catch (SQLException ex) {

        }
        return 0;
    }
    
    public static void radniciSaImenom(String ime) {
        Connection conn = DB.getInstance().getConnection();
        String query = "{ call SPRadniciSaImenom (?) }";
        try ( CallableStatement cs = conn.prepareCall(query)) {
            cs.setString(1, ime);
            try ( ResultSet rs = cs.executeQuery()) {
                System.out.println("Radnici sa imenom " + ime);
                while (rs.next()) {
                    System.out.println("BrLK:" + rs.getInt("BrLK")
                            + " prezime:" + rs.getString("Prezime"));
                }
            } catch (SQLException ex) {
                
            }
        } catch (SQLException ex) {
            
        }
    }
    
                
    public static void main(String[] args) {
        //izmeniInfoORadnicima();
        ispisiRadnike();
        //dodajRadnika("Tamara", "Sekularac", "063555555", 1, 57);
    }

}
