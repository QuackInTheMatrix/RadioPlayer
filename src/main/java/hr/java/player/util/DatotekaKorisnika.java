package hr.java.player.util;

import hr.java.player.entiteti.BaseUser;
import hr.java.player.iznimke.DatotekaException;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
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

    public static Map<String,Integer> dohvatiSve()throws DatotekaException{
        if (!(new File(PATH_KORISNIKA).exists())){
            throw new DatotekaException("Datoteka korisnika ne postoji!");
        }
        Map<String,Integer> korisnici = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new FileReader(PATH_KORISNIKA))) {
            String username;
            Integer passwordHash;
            while ((username = in.readLine()) != null) {
                passwordHash = Integer.parseInt(in.readLine());
                korisnici.put(username,passwordHash);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Greska prilikom citanja");
            alert.setContentText("Dogodila se greska kod citanja sadrzaja datoteke.");
            alert.showAndWait();
            Logging.logger.error(e.getMessage(),e);
        }
        return korisnici;
    }
    public static void unesiKorisnika(BaseUser korisnik)throws DatotekaException{
        if (!(new File(PATH_KORISNIKA).exists())){
            throw new DatotekaException("Datoteka korisnika ne postoji!");
        }
        try {
            String korisnikString = korisnik.username()+'\n'+korisnik.passwordHash()+'\n';
            Files.writeString(Path.of(PATH_KORISNIKA),korisnikString, StandardOpenOption.APPEND);
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Greska prilikom citanja");
            alert.setContentText("Dogodila se greska kod citanja sadrzaja datoteke.");
            alert.showAndWait();
            Logging.logger.error(e.getMessage(),e);
        }
    }
    public static void obrisiKorisnika(String username)throws DatotekaException{
        if (!(new File(PATH_KORISNIKA).exists())){
            throw new DatotekaException("Datoteka korisnika ne postoji!");
        }
        try (BufferedReader in = new BufferedReader(new FileReader(PATH_KORISNIKA))) {
            String procitaniUsername;
            Path pathKorisnika = Path.of(PATH_KORISNIKA);
            List<String> procitaneLinije = new ArrayList<>();
            while ((procitaniUsername = in.readLine()) != null) {
                if (username.equals(procitaniUsername)){
                    in.readLine();
                }else{
                    procitaneLinije.add(procitaniUsername);
                    procitaneLinije.add(in.readLine());
                }
            }
            Files.write(pathKorisnika, procitaneLinije, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Greska prilikom citanja");
            alert.setContentText("Dogodila se greska kod citanja sadrzaja datoteke.");
            alert.showAndWait();
            Logging.logger.error(e.getMessage(),e);
        }
    }
    public static void promjeniUsername(String stariUsername, String noviUsername)throws DatotekaException{
        if (!(new File(PATH_KORISNIKA).exists())){
            throw new DatotekaException("Datoteka korisnika ne postoji!");
        }
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Greska prilikom citanja");
            alert.setContentText("Dogodila se greska kod citanja sadrzaja datoteke.");
            alert.showAndWait();
            Logging.logger.error(e.getMessage(),e);
        }
    }
    public static void promjeniPassword(BaseUser user)throws DatotekaException{
        if (!(new File(PATH_KORISNIKA).exists())){
            throw new DatotekaException("Datoteka korisnika ne postoji!");
        }
        try {
            Path pathKorisnika = Path.of(PATH_KORISNIKA);
            List<String> sveLinije = new ArrayList<>(Files.readAllLines(pathKorisnika));
            for (int i=0;i<sveLinije.size();i++){
                if (sveLinije.get(i).equals(user.username())){
                    sveLinije.set(i+1,String.valueOf(user.passwordHash()));
                    break;
                }
            }
            Files.write(pathKorisnika, sveLinije, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Greska prilikom citanja");
            alert.setContentText("Dogodila se greska kod citanja sadrzaja datoteke.");
            alert.showAndWait();
            Logging.logger.error(e.getMessage(),e);
        }
    }
}
