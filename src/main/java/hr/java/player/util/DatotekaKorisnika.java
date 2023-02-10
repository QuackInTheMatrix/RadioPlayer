package hr.java.player.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static void unesiKorisnika(String username, Integer passwordHash){
        try {
            String korisnikString = username+'\n'+passwordHash+'\n';
            Files.writeString(Path.of(PATH_KORISNIKA),korisnikString, StandardOpenOption.APPEND);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static void promjeniUsername(String stariUsername, String noviUsername){
        try {
            Path pathKorisnika = Path.of(PATH_KORISNIKA);
            List<String> sveLinije = new ArrayList<>(Files.readAllLines(pathKorisnika));
            for (int i=0;i<sveLinije.size();i++){
                if (sveLinije.get(i).equals(stariUsername)){
                    sveLinije.set(i,noviUsername);
                    break;
                }
            }
            Files.write(pathKorisnika, sveLinije, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void promjeniPassword(String username, Integer noviHash){
        try {
            Path pathKorisnika = Path.of(PATH_KORISNIKA);
            List<String> sveLinije = new ArrayList<>(Files.readAllLines(pathKorisnika));
            for (int i=0;i<sveLinije.size();i++){
                if (sveLinije.get(i).equals(username)){
                    sveLinije.set(i+1,String.valueOf(noviHash));
                    break;
                }
            }
            Files.write(pathKorisnika, sveLinije, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
