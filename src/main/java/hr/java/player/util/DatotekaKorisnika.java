package hr.java.player.util;

import hr.java.player.entiteti.Korisnik;
import hr.java.player.entiteti.RazinaOvlasti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class DatotekaKorisnika {
    public static final String PATH_KORISNIKA = "dat/korisnici.txt";

    public static Set<Korisnik> dohvatiSve(){
        Set<Korisnik> korisnici = new HashSet<>();
        try (BufferedReader in = new BufferedReader(new FileReader(PATH_KORISNIKA))) {
            String username, password;

            while ((username = in.readLine()) != null) {
                password = in.readLine();
                Integer razina = Integer.parseInt(in.readLine());
                RazinaOvlasti ovlasti = switch (razina){case 0 -> RazinaOvlasti.USER; case 1 -> RazinaOvlasti.ADMIN;};
                korisnici.add(new Korisnik(username,password,ovlasti));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return korisnici;
    }
    public static void unesiKorisnika(Korisnik korisnik){
        try {
            Files.writeString(Path.of(PATH_KORISNIKA),korisnik.toString(), StandardOpenOption.APPEND);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
