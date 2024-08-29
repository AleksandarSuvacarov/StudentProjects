
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author Aleksa
 */
public class sa200355_UserOperations implements UserOperations{

    public static Connection conn = DB.getInstance().getConnection();
    
    @Override
    public boolean insertUser(String korime, String ime, String prezime, String sifra) {
        String queryStr = "insert into Korisnik(Ime, Prezime, Korime, Sifra) values(?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, ime);
            ps.setString(2, prezime);
            ps.setString(3, korime);
            ps.setString(4, sifra);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                //System.out.println(rs.getInt(1));
                return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_UserOperations.class.getName()).log(Level.SEVERE, null, ex); 
            System.out.println("Neuspelo dodavanje korisnika");
        }
        return false;
    }

    @Override
    public int declareAdmin(String korime) {
        String queryStr = "select idK from Korisnik where Korime=?";
        int idK = -1;
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setString(1, korime);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idK = rs.getInt(1);
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje korisnika iz tabele - fja: declare Admin");
        }
        if(idK == -1)
            return 2;   //No such user
        
        queryStr = "insert into Administrator(idK) values(?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return 0;   //Uspesno dodat admin
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("Neuspelo dodavanje admina, vec postoji - fja: declare Admin");
        }
        return 1;   //Already admin
    }

    @Override
    public Integer getSentPackages(String... korimena) {
        int brPaketa = 0;
        int brKorisnika = 0;
        String queryStr = "select BrPoslatihPaketa from Korisnik where Korime=?";
        for(String korime : korimena){
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setString(1, korime);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    brKorisnika += 1;
                    brPaketa += rs.getInt("BrPoslatihPaketa");
                }
                
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Neuspelo dohvatanje broja poslatih paketa korisnika");
            }
        }
        if(brKorisnika == 0) return null;   //Nije bilo ni jednog postojeceg korisnika
        return brPaketa;    //Dohvacen broj korisnika
    }
    
    public boolean deleteUser(String korime){
        String queryStr = "delete from Korisnik where Korime=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setString(1, korime);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex); 
            System.out.println("Neuspelo brisanje korisnika po korime");
        }
        return false;
    }

    @Override
    public int deleteUsers(String... korimena) {
        int brObrisanih = 0;
        for(String korime : korimena){
            boolean t = deleteUser(korime);
            if(t)brObrisanih++;
        }
        return brObrisanih;
    }

    @Override
    public List<String> getAllUsers() {
        String queryStr = "select * from Korisnik";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<String> korisnici = new ArrayList<>();
            while(rs.next()){
                String korime = rs.getString("Korime");
                korisnici.add(korime);
            }
            return korisnici;
//            if(korisnici.isEmpty()) return null;
//            else return korisnici;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih korisnika");              
        }
        return null;
    }
    
    public static void main(String[] args) {
        sa200355_UserOperations klasa = new sa200355_UserOperations();
        boolean a = klasa.insertUser("markez", "Marko", "Markovic", "markez");
        System.out.println(a);
        
        Integer b = klasa.declareAdmin("markez");
        System.out.println(b);
        
        b = klasa.getSentPackages(new String[]{"markez"});
        System.out.println(b);
       
    }
    
}
