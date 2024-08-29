/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.klijent;

import java.io.Console;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 *
 * @author Aleksa
 */
public class Klijent extends Thread{
    
        
    private static Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("http://localhost:8080/centralniServer/server/")
    .build();
    private static Servis servis = retrofit.create(Servis.class);
    private static Scanner skener = new Scanner(System.in);
    private static boolean gotovo = false;
    private static Klijent nit = null;
    
    private static String aut_username = null;
    private static String aut_password = null;
    private static String aut = null;
    
    private interface Servis{
        //KORISNICI
        @GET("ping")
        Call<ResponseBody> ping(@Header("Authorization") String aut);
        
        @GET("korisnici/{naziv}")
        Call<ResponseBody> dohvati_grad(@Header("Authorization") String aut, @Path("naziv") String naziv);
        
        @POST("korisnici/grad/{naziv}")
        Call<ResponseBody> kreiraj_grad(@Header("Authorization") String aut, @Path("naziv") String naziv);
        
        @POST("korisnici/korisnik/{ime}/{prezime}/{username}/{password}/{adresa}/{grad}/{novac}")
        Call<ResponseBody> kreiraj_korisnika(@Header("Authorization") String aut, @Path("ime") String ime, @Path("prezime") String prezime,
            @Path("username") String username, @Path("password") String password, @Path("adresa") String adresa, @Path("grad") String grad, @Path("novac") double novac);
        
        @PUT("korisnici/novac/{idKor}/{novac}")
        Call<ResponseBody> dodavanje_novca_korisniku(@Header("Authorization") String aut, @Path("idKor") int idKor, @Path("novac") double novac);
        
        @PUT("korisnici/stanovanje/{idK}/{adresa}/{grad}")
        Call<ResponseBody> promena_adrese_i_grada_korisniku(@Header("Authorization") String aut, @Path("idK") int idK, @Path("adresa") String adresa, @Path("grad") String grad);
        
        @GET("korisnici/gradovi")
        Call<ResponseBody> dohvati_sve_gradove(@Header("Authorization") String aut);
        
        @GET("korisnici/korisnici")
        Call<ResponseBody> dohvati_sve_korisnike(@Header("Authorization") String aut);
        
        //ARTIKLI-------------------------------------------------------------
        
        @POST("artikli/kategorija/{naziv}")
        Call<ResponseBody> kreiraj_kategoriju(@Header("Authorization") String aut, @Path("naziv") String naziv);
        
        @POST("artikli/artikal/{naziv}/{opis}/{cena}/{popust}/{kategorija}/{idKor}")
        Call<ResponseBody> kreiraj_artikal(@Header("Authorization") String aut, @Path("naziv") String naziv, @Path("opis") String opis, 
            @Path("cena") double cena, @Path("popust") double popust, @Path("kategorija") String kategorija, @Path("idKor") int idKor);
        
        @PUT("artikli/cena/{naziv}/{cena}")
        Call<ResponseBody> menjanje_cene_artikla(@Header("Authorization") String aut, @Path("naziv") String naziv, @Path("cena") double cena);
        
        @PUT("artikli/popust/{naziv}/{popust}")
        Call<ResponseBody> menjanje_popusta_artikla(@Header("Authorization") String aut, @Path("naziv") String naziv, @Path("popust") double popust);
        
        @POST("artikli/dodavanje/{idKor}/{artikal}/{kolicina}")
        Call<ResponseBody> dodaj_u_korpu(@Header("Authorization") String aut, @Path("idKor") int idKor, @Path("artikal") String artikal,
            @Path("kolicina") int kolicna);
        
        @DELETE("artikli/brisanje/{idKor}/{artikal}/{kolicina}")
        Call<ResponseBody> brisi_iz_korpe(@Header("Authorization") String aut, @Path("idKor") int idKor, @Path("artikal") String artikal,
            @Path("kolicina") int kolicna);
        
        @GET("artikli/kategorije")
        Call<ResponseBody> dohvati_sve_kategorije(@Header("Authorization") String aut);
        
        @GET("artikli/prodavanje")
        Call<ResponseBody> dohvati_moje_artikle_koje_prodajem(@Header("Authorization") String aut);
        
        @GET("artikli/kupovina")
        Call<ResponseBody> dohvati_moje_artikle_iz_korpe(@Header("Authorization") String aut);
        
        //TRANSAKCIJE------------------------------------------------------------
        
        @POST("transakcije/placanje/{adresa}/{grad}/{idKor}")
        Call<ResponseBody> izvrsi_placanje(@Header("Authorization") String aut, @Path("adresa") String adresa, @Path("grad") String grad, @Path("idKor") int idKor);
        
        @GET("transakcije/mojeNarudzbine")
        Call<ResponseBody> dohvati_moje_narudzbine(@Header("Authorization") String aut);
        
        @GET("transakcije/narudzbine")
        Call<ResponseBody> dohvati_sve_narudzbine(@Header("Authorization") String aut);
        
        @GET("transakcije/transakcije")
        Call<ResponseBody> dohvati_sve_transakcije(@Header("Authorization") String aut);
        
    }
    
    //KORISNICI----------------------------------------------------------
    
    private static void postavi_kredencijale(){
        System.out.println("Unesite username: ");
        aut_username = skener.nextLine();
        System.out.println("Unesite password: ");
        aut_password = skener.nextLine();
        aut = "Basic " + Base64.getEncoder().encodeToString((aut_username + ":" + aut_password).getBytes());
    }
    
    private static Call<ResponseBody> ping(){
        return servis.ping(aut);
    }
    
    private static Call<ResponseBody> dohvati_grad(){ 
        System.out.println("Unesite ime grada: ");
        String naziv = skener.nextLine();
        return servis.dohvati_grad(aut, naziv);
    }
    
    private static Call<ResponseBody> kreiraj_grad(){ 
        System.out.println("Unesite ime grada: ");
        String naziv = skener.nextLine();
        return servis.kreiraj_grad(aut, naziv);
    }
    
    private static Call<ResponseBody> kreiraj_korisnika(){ 
        System.out.println("Unesite ime: ");
        String ime = skener.nextLine();
        System.out.println("Unesite prezime: ");
        String prezime = skener.nextLine();
        System.out.println("Unesite username: ");
        String username = skener.nextLine();
        System.out.println("Unesite password: ");
        String password = skener.nextLine();
        System.out.println("Unesite adresu: ");
        String adresa = skener.nextLine();
        System.out.println("Unesite ime grada: ");
        String grad = skener.nextLine();
        System.out.println("Unesite novac: ");
        String novacStr = skener.nextLine();
        double novac = Double.parseDouble(novacStr);
        return servis.kreiraj_korisnika(aut, ime, prezime, username, password, adresa, grad, novac);
    }
    
    private static Call<ResponseBody> dodavanje_novca_korisniku(){ 
        System.out.println("Unesite idKorisnika: ");
        String idKorStr = skener.nextLine();
        int idKor = Integer.parseInt(idKorStr);        
        System.out.println("Unesite novac: ");
        String novacStr = skener.nextLine();
        double novac = Double.parseDouble(novacStr);
        return servis.dodavanje_novca_korisniku(aut, idKor, novac);
    }
    
    private static Call<ResponseBody> promena_adrese_i_grada_korisniku(){ 
        System.out.println("Unesite idKorisnika: ");
        String idKorStr = skener.nextLine();
        int idKor = Integer.parseInt(idKorStr);         
        System.out.println("Unesite adresu: ");
        String adresa = skener.nextLine();
        System.out.println("Unesite ime grada: ");
        String grad = skener.nextLine();
        return servis.promena_adrese_i_grada_korisniku(aut, idKor, adresa, grad);
    }
    
    private static Call<ResponseBody> dohvati_sve_gradove(){
        return servis.dohvati_sve_gradove(aut);
    }
    
    private static Call<ResponseBody> dohvati_sve_korisnike(){
        return servis.dohvati_sve_korisnike(aut);
    }
    
    //ARTIKLI----------------------------------------------------------------
    
    private static Call<ResponseBody> kreiraj_kategoriju(){ 
        System.out.println("Unesite naziv kategorije: ");
        String naziv = skener.nextLine();
        return servis.kreiraj_kategoriju(aut, naziv);
    }
    
    private static Call<ResponseBody> kreiraj_artikal(){ 
        System.out.println("Unesite naziv: ");
        String naziv = skener.nextLine();
        System.out.println("Unesite opis: ");
        String opis = skener.nextLine();
        System.out.println("Unesite cenu: ");
        String cenaStr = skener.nextLine();
        double cena = Double.parseDouble(cenaStr);
        System.out.println("Unesite popust: ");
        String popustStr = skener.nextLine();
        double popust = Double.parseDouble(popustStr);        
        System.out.println("Unesite ime kategorije: ");
        String kategorija = skener.nextLine();
        System.out.println("Unesite idKorisnika koji prodaje artikal: ");
        String idKorStr = skener.nextLine();
        int idKor = Integer.parseInt(idKorStr);
        return servis.kreiraj_artikal(aut, naziv, opis, cena, popust, kategorija, idKor);
    }
    
    private static Call<ResponseBody> menjanje_cene_artikla(){ 
        System.out.println("Unesite naziv: ");
        String naziv = skener.nextLine();
        System.out.println("Unesite cenu: ");
        String cenaStr = skener.nextLine();
        double cena = Double.parseDouble(cenaStr);
        return servis.menjanje_cene_artikla(aut, naziv, cena);
    }
    
    private static Call<ResponseBody> menjanje_popusta_artikla(){ 
        System.out.println("Unesite naziv: ");
        String naziv = skener.nextLine();
        System.out.println("Unesite popust: ");
        String popustStr = skener.nextLine();
        double popust = Double.parseDouble(popustStr);
        return servis.menjanje_popusta_artikla(aut, naziv, popust);
    }
    
    private static Call<ResponseBody> dodaj_u_korpu(){ 
        System.out.println("Unesite idKorisnika u ciji korpu se dodaje: ");
        String idKorStr = skener.nextLine();
        int idKor = Integer.parseInt(idKorStr);
        System.out.println("Unesite naziv artikla: ");
        String artikal = skener.nextLine();
        System.out.println("Unesite kolicinu za koju povecavate: ");
        String kolicinaStr = skener.nextLine();
        int kolicina = Integer.parseInt(kolicinaStr);
        return servis.dodaj_u_korpu(aut, idKor, artikal, kolicina);
    }
    
    private static Call<ResponseBody> brisi_iz_korpe(){ 
        System.out.println("Unesite idKorisnika iz cije korpe se uklanja: ");
        String idKorStr = skener.nextLine();
        int idKor = Integer.parseInt(idKorStr);
        System.out.println("Unesite naziv artikla: ");
        String artikal = skener.nextLine();
        System.out.println("Unesite kolicinu za koju smanjujete: ");
        String kolicinaStr = skener.nextLine();
        int kolicina = Integer.parseInt(kolicinaStr);
        return servis.brisi_iz_korpe(aut, idKor, artikal, kolicina);
    }
    
    private static Call<ResponseBody> dohvati_sve_kategorije(){
        return servis.dohvati_sve_kategorije(aut);
    }
    
    private static Call<ResponseBody> dohvati_moje_artikle_koje_prodajem(){
        return servis.dohvati_moje_artikle_koje_prodajem(aut);
    }
    
    private static Call<ResponseBody> dohvati_moje_artikle_iz_korpe(){
        return servis.dohvati_moje_artikle_iz_korpe(aut);
    }
    
    //TRANSAKCIJE----------------------------------------------------------------
    
    private static Call<ResponseBody> izvrsi_placanje(){
        System.out.println("Unesite adresu: ");
        String adresa = skener.nextLine();
        System.out.println("Unesite ime grada: ");
        String grad = skener.nextLine();
        System.out.println("Unesite idKorisnika koji vrsi uplatu: ");
        String idKorStr = skener.nextLine();
        int idKor = Integer.parseInt(idKorStr);         
        return servis.izvrsi_placanje(aut, adresa, grad, idKor);
    }
    
    private static Call<ResponseBody> dohvati_moje_narudzbine(){
        return servis.dohvati_moje_narudzbine(aut);
    }
    
    private static Call<ResponseBody> dohvati_sve_narudzbine(){
        return servis.dohvati_sve_narudzbine(aut);
    }
    
    private static Call<ResponseBody> dohvati_sve_transakcije(){
        return servis.dohvati_sve_transakcije(aut);
    }
    
    
    private static void meni(){
        System.out.println("Ping....................................................................0");
        System.out.println("Kreiranje grada.........................................................1");
        System.out.println("Kreiranje korisnika.....................................................2");
        System.out.println("Dodavanje novca korisniku...............................................3");
        System.out.println("Promena adrese i grada korisniku........................................4");
        System.out.println("Kreiranje kategorije....................................................5");
        System.out.println("Kreiranje artikla.......................................................6");
        System.out.println("Menjanje cene artikla...................................................7");
        System.out.println("Menjanje popusta artikla................................................8");
        System.out.println("Dodavanje artikla u korpu...............................................9");
        System.out.println("Brisanje artikla iz korpe..............................................10");
        System.out.println("Placanje...............................................................11");
        System.out.println("Dohvatanje svih gradova................................................12");
        System.out.println("Dohvatanje svih korisnika..............................................13");
        System.out.println("Dohvatanje svih kategorija.............................................14");
        System.out.println("Dohvatanje svih artikala koje prodaje prijavljeni korisnik.............15");
        System.out.println("Dohvatanje sadrzaja korpe prijavljenog korisnika.......................16");
        System.out.println("Dohvatanje svih narudzbina koje ima prijavljeni korisnik...............17");
        System.out.println("Dohvatanje svih narudzbina.............................................18");
        System.out.println("Dohvatanje svih transakcija............................................19");
        System.out.println("Prijavljvanje korisnika(Autentifikacija)...............................21");        
    }
    
    private static void posalji_zahtev(int br_zahteva){
        Call<ResponseBody> call = null;
        gotovo = false;
        
        if(br_zahteva < 0 || br_zahteva > 22){
            System.out.println("Pogresan broj zahteva, pogledajte MENI na 22!!!");
            gotovo=true;
            return;
        }        
        
        switch(br_zahteva){
            case 0:
                call = ping();
                break;
            case 1:
                call = kreiraj_grad();
                break;
            case 2:
                call = kreiraj_korisnika();
                break;
            case 3:
                call = dodavanje_novca_korisniku();
                break;
            case 4:
                call = promena_adrese_i_grada_korisniku();
                break;
            case 5:
                call = kreiraj_kategoriju();
                break;
            case 6:
                call = kreiraj_artikal();
                break;
            case 7:
                call = menjanje_cene_artikla();
                break;
            case 8:
                call = menjanje_popusta_artikla();
                break;
            case 9:
                call = dodaj_u_korpu();
                break;
            case 10:
                call = brisi_iz_korpe();
                break;
            case 11:
                call = izvrsi_placanje();
                break;
            case 12:
                call = dohvati_sve_gradove();
                break;
            case 13:
                call = dohvati_sve_korisnike();
                break;
            case 14:
                call = dohvati_sve_kategorije();
                break;
            case 15:
                call = dohvati_moje_artikle_koje_prodajem();
                break;
            case 16:
                call = dohvati_moje_artikle_iz_korpe();
                break;
            case 17:
                call = dohvati_moje_narudzbine();
                break;
            case 18:
                call = dohvati_sve_narudzbine();
                break;
            case 19:
                call = dohvati_sve_transakcije();
                break;
            case 20:
                call = dohvati_grad();
                break;
            case 21:
                postavi_kredencijale();
                gotovo = true;
                return;
            case 22:
                meni();
                gotovo = true;
                return;
        }
        
        call.enqueue(new Callback<ResponseBody>(){

            @Override
            public void onResponse(Call<ResponseBody> arg0, retrofit2.Response<ResponseBody> response) {
                try {
                    System.out.println("Kod = " + response.code());
                    if(response.body() != null)
                        System.out.println(response.body().string());
                    else
                        System.out.println(response.errorBody().string());
                    
                    gotovo = true;
                    synchronized(nit){
                        nit.notify();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
                gotovo = true;
                synchronized(nit){
                        nit.notify();
                    }
                System.out.println("Zahtev iz nekog razloga nije stigao. Pokusajte ponovo!");
            }
        });
        
        
    }
    
    @Override
    public void run(){
        while(true){
            System.out.println("\nUnesite broj zahteva (za MENI 22): ");
            String br_zahteva = skener.nextLine();
            System.out.println("");
            
            try{
                posalji_zahtev(Integer.parseInt(br_zahteva));
            }
            catch(NumberFormatException e){
                System.out.println("Unesite validan broj zahteva!");
                continue;
            }
                
            try {
                synchronized(this){
                    while(!gotovo){
                        this.wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public static void main(String[] args) { 
        
        nit = new Klijent();
        nit.start();
        
    }
    
}
