package hr.java.player.util;

import hr.java.player.entiteti.Korisnik;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class DatotekaKorisnika {
    public static final String PATH_KORISNIKA = "dat/korisnici.txt";

    public static Map<String,Integer> dohvatiSve(){
        Map<String,Integer> korisnici = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new FileReader(PATH_KORISNIKA))) {
            String username;
            Integer passwordHash;
            while ((username = in.readLine()) != null) {
                passwordHash = Integer.parseInt(in.readLine());
                korisnici.put(username,passwordHash);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return korisnici;
    }
    public static void unesiKorisnika(Korisnik korisnik){
        try {
            String korisnikString = korisnik.getUsername()+'\n'+korisnik.getPasswordHash()+'\n';
            Files.writeString(Path.of(PATH_KORISNIKA),korisnikString, StandardOpenOption.APPEND);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
