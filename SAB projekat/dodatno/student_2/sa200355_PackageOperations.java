
package student;


import java.sql.Types;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;

import static student.sa200355_Util.*;
/**
 *
 * @author Aleksa
 */
public class sa200355_PackageOperations implements PackageOperations{
    
    public static Connection conn = DB.getInstance().getConnection();
    public static final Random RANDOM = new Random();
    
    public static class sa200355_Pair<A extends Integer, B extends BigDecimal> implements Pair<A, B> {
        private A p1;
        private B p2;

        public sa200355_Pair(A arg1, B arg2){
            this.p1 = arg1;
            this.p2 = arg2;
        }

        @Override
        public A getFirstParam() {
            return p1;
        }

        @Override
        public B getSecondParam() {
            return p2;
        }
    }   

    @Override
    public int insertPackage(int opstinaOd, int opstinaDo, String korime, int tipPaketa, BigDecimal tezina) {
        int idK = utilGetUserId(korime);
        if(idK == -1) return -1; // Nepostojeci korisnik
        if(!utilDistrictExists(opstinaOd)) return -1; //Nepostojeca opstinaOd
        if(!utilDistrictExists(opstinaDo)) return -1; //Nepostojeca opstinaDo
        
        String queryStr = "insert into Paket(IdOpsOd, IdOpsDo, TipPaketa, IdKorisnika, Tezina) values(?, ?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, opstinaOd);
            ps.setInt(2, opstinaDo);
            ps.setInt(3, tipPaketa);
            ps.setInt(4, idK);
            ps.setFloat(5, tezina.floatValue());
            int rowsAffected = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rowsAffected != 0 && rs.next())
                return rs.getInt(1);    //Id novog paketa
               
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        return -1;
    }

    @Override
    public int insertTransportOffer(String korimeKurir, int idPaketa, BigDecimal procenat) {
        if (procenat == null) {
            // Generiši nasumičnu decimalnu vrednost između -10 i 10
            double min = -10.0;
            double max = 10.0;
            double vrednost = min + (max - min) * RANDOM.nextDouble();
            procenat = new BigDecimal(vrednost);
        }
        
        int idK = utilGetUserId(korimeKurir);
        if(idK == -1) return -1;    //Ne postoji takav korisnik
        if(!utilCourierExists(idK)) return -1; //Korisnik nije kurir
        if(!utilPackageExists(idPaketa)) return -1; //Paket ne postoji
        if(utilGetCourierStatus(idK) == 1) return -1; //Kurir je u statusu vozi
        
        String queryStr = "insert into Ponuda(IdP, IdK, Procenat) values(?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, idPaketa);
            ps.setInt(2, idK);
            ps.setFloat(3, procenat.floatValue());
            int rowsAffected = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rowsAffected != 0 && rs.next())
                return rs.getInt(1);    //Id nove ponude
               
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return -1;
    }

    public static int[] PocentaCena = {10, 25, 75};
    public static int[] TezinskiFaktor = {0, 1, 2};
    public static int[] CenaPoKilogramu = {0, 100, 300};
    public static int[] CenaGorivaPoLitru = {15, 32, 36};
    
    @Override
    public boolean acceptAnOffer(int idPon) {
        int idP = -1;
        float procenat = -1;
        int idK = -1;
        
        String queryStr = "Select * from Ponuda where idPon=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idPon);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idP = rs.getInt("IdP");
                procenat = rs.getFloat("Procenat");
                idK = rs.getInt("IdK");
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idP == -1 || idK == -1) return false; //Nepostojeca ponuda
        
        int idOpsOd = 0;
        int idOpsDo = 0;
        int tipPaketa = 0;
        double tezina = 0;
        int status = -1;
        
        queryStr = "select * from Paket where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idP);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idOpsOd = rs.getInt("IdOpsOd");
                idOpsDo = rs.getInt("IdOpsDo");
                status = rs.getInt("Status");
                tezina = rs.getFloat("Tezina");
                tipPaketa = rs.getInt("TipPaketa");
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(status != 0) return false; //Paket nije u statusu kreiran i ne moze biti prihvacen
        
        int x1 = utilGetDistrictX(idOpsOd);
        int y1 = utilGetDistrictY(idOpsOd);
        int x2 = utilGetDistrictX(idOpsDo);
        int y2 = utilGetDistrictY(idOpsDo);
        
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        
        double cena = (PocentaCena[tipPaketa] + TezinskiFaktor[tipPaketa]*tezina*CenaPoKilogramu[tipPaketa])*utilEuclidean(x1, y1, x2, y2);
        cena = cena * (procenat * 1. / 100 + 1);
        
        queryStr = "update Paket\n" +
                    "set IdKurira=?, Procenat=?, Cena=?, Vreme=?, Status=?\n" +
                    "where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            ps.setFloat(2, procenat);
            ps.setDouble(3, cena);
            ps.setTimestamp(4, timestamp);
            ps.setInt(5, 1);
            ps.setInt(6, idP);
            
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0){
               return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public List<Integer> getAllOffers() {
        String queryStr = "select * from Ponuda";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<Integer> ponude = new ArrayList<>();
            while(rs.next()){
                int idPon = rs.getInt(1);
                ponude.add(idPon);
            }
            return ponude;
//            if(ponude.isEmpty()) return null;
//            else return ponude;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih ponuda");              
        }
        return null;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int idP) {
        String queryStr = "select * from Ponuda where IdP=?";
        try (PreparedStatement ps = conn.prepareStatement(queryStr)) {
            ps.setInt(1, idP);
            ResultSet rs = ps.executeQuery();
            List<Pair<Integer, BigDecimal>> ponude = new ArrayList<>();
            while (rs.next()) {
                int idPon = rs.getInt("IdPon");
                float prc = rs.getFloat("Procenat");
                ponude.add(new sa200355_Pair<>(idPon, new BigDecimal(prc)));
            }
            return ponude;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje ponuda za paket: ");
        }
        return null;
    }

    @Override
    public boolean deletePackage(int idP) {
        String queryStr = "delete from Paket where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idP);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
                return true;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo brisanje paketa po idP");
        }
        return false;
    }

    @Override
    public boolean changeWeight(int idP, BigDecimal novaTezina) {
        
        String queryStr = "update Paket set Tezina=? where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setFloat(1, novaTezina.floatValue());
            ps.setInt(2, idP);
            int rowsAffected = ps.executeUpdate();
            
            if(rowsAffected != 0)
                return true;    //Promenjena tezina
               
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    @Override
    public boolean changeType(int idP, int tipPaketa) {
        String queryStr = "update Paket set TipPaketa=? where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, tipPaketa);
            ps.setInt(2, idP);
            int rowsAffected = ps.executeUpdate();
            
            if(rowsAffected != 0)
                return true;    //Promenjen tip
               
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    @Override
    public Integer getDeliveryStatus(int idP) {
        String queryStr = "Select Status from Paket where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idP);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt(1); //Dobijen status
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int idP) {
        String queryStr = "Select * from Paket where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idP);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                float cena = rs.getFloat("Cena");
                if (rs.wasNull()) {
                    // Cena je bila null u bazi
                    return null; // ili neku drugu vrednost koja označava null u vašem kontekstu
                } else {
                    return new BigDecimal(cena);
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int idP) {
        String queryStr = "Select * from Paket where idP=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idP);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp timestamp = rs.getTimestamp("Vreme");
                if (timestamp == null) {
                    return null; 
                } else {
                    return new Date(timestamp.getTime());
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int tipPaketa) {
        String queryStr = "select * from Paket where TipPaketa=?";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, tipPaketa);
            ResultSet rs = ps.executeQuery();
            List<Integer> paketi = new ArrayList<>();
            while(rs.next()){
                int idP = rs.getInt(1);
                paketi.add(idP);
            }
            return paketi;
//            if(paketi.isEmpty()) return null;
//            else return paketi;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje paketa sa zadatim tipom");              
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackages() {
        String queryStr = "select * from Paket";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ResultSet rs = ps.executeQuery();
            List<Integer> paketi = new ArrayList<>();
            while(rs.next()){
                int idP = rs.getInt(1);
                paketi.add(idP);
            }
            return paketi;
//            if(paketi.isEmpty()) return null;
//            else return paketi;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo dohvatanje svih paketa");              
        }
        return null;
    }

    @Override
    public List<Integer> getDrive(String korime) {
        int idK = utilGetUserId(korime);
        if(idK == -1) return null; //Nepostojeci korisnik
        if(!utilCourierExists(idK)) return null; //Korisnik nije kurir
        
        String queryStr = "select *\n" +
                            "from AktuelnaVoznja av join Paket p on(av.IdP=p.IdP)\n" +
                            "where av.IdK=? and p.Status != 3";
        try(PreparedStatement ps = conn.prepareStatement(queryStr)){
            ps.setInt(1, idK);
            List<Integer> paketiVoznje = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int idP = rs.getInt("IdP");
                paketiVoznje.add(idP);
            }
            if(paketiVoznje.isEmpty()) return null;
            else return paketiVoznje;
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Greska pri dohvatanju paketa iz aktuelne voznje - getDrive");
        }
        return null;
    }

    @Override
    public int driveNextPackage(String korime) {
        int idK = utilGetUserId(korime);
        if(idK == -1) return -2;    //Nema korisnika
        if(!utilCourierExists(idK)) return -2; //Korisnik nije kurir
        
        int statusKurira = utilGetCourierStatus(idK);
        List<Integer> paketi = new ArrayList<>();
        
        if(statusKurira == 0){
            
            String queryStr = "select *\n" +
                                "from Paket\n" +
                                "where Status=? and idKurira=?\n" +
                                "order by Vreme asc";
            
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setInt(1, 1);
                ps.setInt(2, idK);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    paketi.add(rs.getInt("IdP"));
                }
                if(paketi.isEmpty()) return -1; //Nema niti jednog paketa za isporuku
                
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            queryStr = "insert into AktuelnaVoznja(IdP, IdK) values(?, ?)";
            for(int idP : paketi){
                try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                    ps.setInt(1, idP);
                    ps.setInt(2, idK);
                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected == 0){
                        System.out.println("Nije nista insertovano u AktuelnaVoznja - pocetak voznje");
                        return -2;
                    }
                } catch (SQLException ex) {
                    //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Greska pri ubacivaju u AktuelnaVoznja");
                    return -2;
                }
            }
            if(!utilSetCourierStatus(idK, 1)) {
                System.out.println("Nije moguce postaviti status kuriru - driveNextPackage");
                return -2;
            }
            
            queryStr = "update Paket set Status=2 where IdP=?";
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setInt(1, paketi.get(0));
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected == 0){System.out.println("Neuspelo postavljanje prvog paketa na pokupljen - driveNextPackage"); return -2;}
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Neuspelo postavljanje prvog paketa na pokupljen - driveNextPackage");
                return -2;
            }
            
        }
        String queryStr = "select *\n" +
                            "from AktuelnaVoznja av join Paket p on(av.IdP=p.IdP) join Korisnik k on(p.IdKorisnika=k.IdK)\n" +
                            "where p.Status=2 and av.idK=?";
        int idP;
        try(PreparedStatement ps = conn.prepareStatement(queryStr, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int idKorisnika = rs.getInt("IdKorisnika");
                idP = rs.getInt("IdP");
                rs.updateInt("Status", 3);
                rs.updateRow();                
                try(PreparedStatement pss = conn.prepareStatement("Update Korisnik\n" +
                                                                    "set BrPoslatihPaketa=BrPoslatihPaketa+1\n" +
                                                                    "where idK=?")){
                    pss.setInt(1, idKorisnika);
                    int rowsAffected = pss.executeUpdate();
                    if(rowsAffected == 0){
                        System.out.println("Nije azuriran BrPoslatihPaketa Korisnika - driveNextPackage");
                        return -2;
                    }
                }
                try(PreparedStatement pss = conn.prepareStatement("Update Kurir\n" +
                                                                    "set BrIsporuka=BrIsporuka+1\n" +
                                                                    "where idK=?")){
                    pss.setInt(1, idK);
                    int rowsAffected = pss.executeUpdate();
                    if(rowsAffected == 0){
                        System.out.println("Nije azuriran BrIsporuka Kurira - driveNextPackage");
                        return -2;
                    }
                }
                
            }
            else {System.out.println("Neuspelo postavljanje paketa na isporucen - driveNextPackage"); return -2;}
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo postavljanje paketa na isporucen - driveNextPackage");
            return -2;
        }
        
        queryStr = "select top 1 *\n" +
                    "from AktuelnaVoznja av join Paket p on(av.IdP=p.IdP)\n" +
                    "where p.Status=1 and av.idK=?\n" +
                    "order by p.Vreme asc";
        boolean kraj = false;
        try(PreparedStatement ps = conn.prepareStatement(queryStr, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)){
            ps.setInt(1, idK);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                rs.updateInt("Status", 2);
                rs.updateRow();                
            }
            else {
                kraj = true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Neuspelo kupljenje sledeceg paketa za voznju - driveNextPackage");
            return -2;
        }
        
        if(kraj){
            double profit = 0;
            List<Integer> idOpsOd = new ArrayList<>();
            List<Integer> idOpsDo = new ArrayList<>();
            List<Double> cena = new ArrayList<>();
            int tipGoriva = utilGetCourierFuelType(idK);
            if(tipGoriva == -1) {System.out.println("Nije bilo moguce dohvatiti tip goriva - driveNextPackage"); return -2;}
            double potrosnja = utilGetCourierFuelConsumption(idK);
            if(potrosnja == -1) {System.out.println("Nije bilo moguce dohvatiti potrosnju goriva - driveNextPackage"); return -2;}
            
            queryStr = "select *\n" +
                    "from AktuelnaVoznja av join Paket p on(av.IdP=p.IdP)\n" +
                    "where p.Status=3 and av.idK=?\n" +
                    "order by p.Vreme asc";
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setInt(1, idK);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    idOpsOd.add(rs.getInt("IdOpsOd"));
                    idOpsDo.add(rs.getInt("IdOpsDo"));
                    cena.add(rs.getDouble("Cena"));
                }
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Neuspelo dohvatanje cena i opstina na kraju voznje - driveNextPackage");
                return -2;
            }
            int x1, y1, x2, y2;
            double distance;
            for(int i = 0; i < cena.size(); i++){
                x1 = utilGetDistrictX(idOpsOd.get(i));
                y1 = utilGetDistrictY(idOpsOd.get(i));
                x2 = utilGetDistrictX(idOpsDo.get(i));
                y2 = utilGetDistrictY(idOpsDo.get(i));
                
                profit += cena.get(i);
                distance = utilEuclidean(x1, y1, x2, y2);
                profit = profit - distance * CenaGorivaPoLitru[tipGoriva] * potrosnja;
                
                if(i + 1 < cena.size()){
                    x1 = utilGetDistrictX(idOpsOd.get(i + 1));
                    y1 = utilGetDistrictY(idOpsOd.get(i + 1));
                    distance = utilEuclidean(x1, y1, x2, y2);
                    profit = profit - distance * CenaGorivaPoLitru[tipGoriva] * potrosnja;
                }
                
            }
            
            queryStr = "update Kurir set Profit=Profit+? where IdK=?";
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setDouble(1, profit);
                ps.setInt(2, idK);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected == 0){System.out.println("Neuspelo azuriranje profita Kurira - driveNextPackage"); return -2;}
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Neuspelo azuriranje profita Kurira - driveNextPackage");
                return -2;
            }
            
            queryStr = "delete from AktuelnaVoznja where idK=?";
            try(PreparedStatement ps = conn.prepareStatement(queryStr)){
                ps.setInt(1, idK);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected == 0){System.out.println("Neuspelo brisanje iz AktuelnaVoznja - driveNextPackage"); return -2;}
            } catch (SQLException ex) {
                //Logger.getLogger(sa200355_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Neuspelo brisanje iz AktuelnaVoznja - driveNextPackage");
                return -2;
            }         
            utilSetCourierStatus(idK, 0);
     
        }
        
        return idP;
    }
    
    public static void main(String[] args) {
        sa200355_PackageOperations klasa = new sa200355_PackageOperations();
        int b;
//        b = klasa.insertPackage(34, 33, "b", 0, new BigDecimal(0.0));
//        System.out.println(b);
//        
//        b = klasa.insertTransportOffer("a", b, null);
//        System.out.println(b);
//
//        boolean a;
//        a = klasa.acceptAnOffer(b);
//        System.out.println(a);

        List<Pair<Integer, BigDecimal>> ponude = klasa.getAllOffersForPackage(10);
        for(Pair<Integer, BigDecimal> pair: ponude){
            Integer idPon = pair.getFirstParam();
            BigDecimal procenat = pair.getSecondParam();
            System.out.println("IdPon: " + idPon + ", Procenat: " + procenat);
        }

        List<Integer> lista = klasa.getAllOffers();
        for(int i : lista){
            System.out.println(i);
        }
        
//        a = klasa.changeType(10, 1);
//        System.out.println(a);
//        a = klasa.changeWeight(10, new BigDecimal(15.5));
//        System.out.println(a);
        
        BigDecimal bd;
        bd = klasa.getPriceOfDelivery(10);
        System.out.println(bd);
        
        Date datum = klasa.getAcceptanceTime(19);
        System.out.println(datum);  
        
        lista = klasa.getDrive("a");
        for(int i : lista){
            System.out.println(i);
        }
        
        
        
        b = klasa.driveNextPackage("a");
        System.out.println(b);
        
    }
    
}
