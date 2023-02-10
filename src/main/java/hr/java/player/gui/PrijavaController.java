package hr.java.player.gui;

import hr.java.player.entiteti.Korisnik;
import hr.java.player.util.DatotekaKorisnika;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Map;

public class PrijavaController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameLabel, passwordLabel, naslovLabel;
    @FXML
    private Button prijavaGumb;

    @FXML
    void initialize(){
        Korisnik prijavljeniKorisnik = GlavnaAplikacija.getKorisnik();
        if (prijavljeniKorisnik!=null){
            usernameField.setVisible(false);
            passwordField.setVisible(false);
            usernameLabel.setVisible(false);
            passwordLabel.setVisible(false);
            prijavaGumb.setVisible(false);
            naslovLabel.setText("Dobrodosli "+prijavljeniKorisnik.getIme());
        }
    }

    @FXML
    void prijava(){
        Map<String,Integer> sviKorisnici = DatotekaKorisnika.dohvatiSve();
        if (sviKorisnici.containsKey(usernameField.getText()) && sviKorisnici.get(usernameField.getText())==passwordField.getText().hashCode()){
            System.out.println("Uspjesna prijava!");
            GlavnaAplikacija.prijaviKorisnika(usernameField.getText());
            try {
                BorderPane root = FXMLLoader.load(getClass().getResource("prijava.fxml"));
                GlavnaAplikacija.setNewStage(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("Neuspjesna prijava!");
            // TODO: alert poruka umjesto sout
        }
    }
}
