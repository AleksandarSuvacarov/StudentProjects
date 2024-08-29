
package student;

import java.sql.Connection;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author Aleksa
 */
public class sa200355_VehicleOperations implements VehicleOperations{

    public static Connection conn = DB.getInstance().getConnection();
    
    @Override
    public boolean insertVehicle(String regBr, int tipGoriva, BigDecimal potrosnja) {
        String queryStr = "insert into Vozilo(RegBr, TipGoriva, Potrosnja) values(?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, regBr);
            ps.setInt(2, tipGoriva);
            ps.setFloat(3, potrosnja.floatValue());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                //System.out.println(rs.getInt(1));
                return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex); 
            System.out.println("Neuspelo dodavanje vozila");
        }
        return false;
    }
    
    public boolean deleteVehichle(String regBr){
        String queryStr = "delete from Vozilo where RegBr=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setString(1, regBr);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex); 
            System.out.println("Neuspelo brisanje vozila po regBr");
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... regBrojevi) {
        int brObrisanih = 0;
        for(String regBr : regBrojevi){
            boolean t = deleteVehichle(regBr);
            if(t)brObrisanih++;
        }
        return brObrisanih;
    }

    @Override
    public List<String> getAllVehichles() {
        String queryStr = "select * from Vozilo";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<String> vozila = new ArrayList<>();
            while(rs.next()){
                String regBr = rs.getString(2);
                vozila.add(regBr);
            }
            return vozila;
//            if(vozila.isEmpty()) return null;
//            else return vozila;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih vozila");              
        }
        return null;
    }

    @Override
    public boolean changeFuelType(String regBr, int tipGoriva) {
        String queryStr = "update Vozilo\n" +
                            "set TipGoriva = ?\n" +
                            "where RegBr = ?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, tipGoriva);
            ps.setString(2, regBr);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspela promena tipa goriva za vozilo");              
        }
        return false;
    }

    @Override
    public boolean changeConsumption(String regBr, BigDecimal potrosnja) {
        String queryStr = "update Vozilo\n" +
                             "set Potrosnja = ?\n" +
                             "where RegBr = ?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setFloat(1, potrosnja.floatValue());
            ps.setString(2, regBr);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspela promena potrosnje za vozilo");              
        }
        return false;
   }
    
    public static void main(String[] args) {
        sa200355_VehicleOperations klasa = new sa200355_VehicleOperations();
        boolean a = klasa.insertVehicle("BG1360DJ", 2, new BigDecimal(6.5));
        System.out.println(a);
        a = klasa.insertVehicle("BG1370DJ", 1, new BigDecimal(7.5));
        System.out.println(a);
        a = klasa.changeFuelType("BG1360DJ", 0);
        System.out.println(a);
        a = klasa.changeConsumption("BG1360DJ", new BigDecimal(10));
        System.out.println(a);
        a = klasa.changeFuelType("BG130DJ", 0);
        System.out.println(a);
        
        int rowsAffected = klasa.deleteVehicles(new String[]{"BG1360DJ", "BG130DJ", "BG1370DJ"});
        System.out.println(rowsAffected);
        List<String> b = klasa.getAllVehichles();
        for (String t : b) {
            System.out.println(t);
        }
    }
    
}
