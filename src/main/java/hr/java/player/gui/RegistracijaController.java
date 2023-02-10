package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.RazinaOvlasti;
import hr.java.player.util.DatotekaKorisnika;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Map;

public class RegistracijaController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField imeField;
    @FXML
    private TextField prezimeField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField ponovljeniPasswordField;

    @FXML
    void registracija(){
        Map<String,Integer> sviKorisnici = DatotekaKorisnika.dohvatiSve();
        if (!imeField.getText().isEmpty() && !prezimeField.getText().isEmpty() && !emailField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && passwordField.getText().equals(ponovljeniPasswordField.getText()) && !sviKorisnici.containsKey(usernameField.getText())){
            DatotekaKorisnika.unesiKorisnika(usernameField.getText(),passwordField.getText().hashCode());
            BazaPodataka.unesiKorisnika(usernameField.getText(),emailField.getText(),imeField.getText(),prezimeField.getText(),passwordField.getText().hashCode(),RazinaOvlasti.USER);
            //TODO: zamjeniti sout sa alert
            System.out.println("Uspjesan unos novog korisnika!");

        }else{
            System.out.println("Potrebno je popuniti sva polja za unos, unjeti unikatno ime i ispravno ponoviti passworde");
        }
    }
}
