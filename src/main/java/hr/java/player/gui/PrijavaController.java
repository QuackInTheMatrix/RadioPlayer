package hr.java.player.gui;

import hr.java.player.util.DatotekaKorisnika;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Map;

public class PrijavaController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    void prijava(){
        Map<String,Integer> sviKorisnici = DatotekaKorisnika.dohvatiSve();
        if (sviKorisnici.containsKey(usernameField.getText()) && sviKorisnici.get(usernameField.getText())==passwordField.getText().hashCode()){
            System.out.println("Uspjesna prijava!");
            //TODO: kreiranje korisnickog objekta iz baze i ispit Alert poruke umjesto sout
        }else{
            System.out.println("Neuspjesna prijava!");
            // TODO: alert poruka umjesto sout
        }
    }
}
