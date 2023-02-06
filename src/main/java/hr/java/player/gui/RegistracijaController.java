package hr.java.player.gui;

import hr.java.player.entiteti.Korisnik;
import hr.java.player.entiteti.RazinaOvlasti;
import hr.java.player.util.DatotekaKorisnika;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Set;
import java.util.stream.Collectors;

public class RegistracijaController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField ponovljeniPasswordField;

    @FXML
    void registracija(){
        Set<Korisnik> sviKorisnici = DatotekaKorisnika.dohvatiSve();
        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && passwordField.getText().equals(ponovljeniPasswordField.getText()) && sviKorisnici.stream().filter(korisnik -> korisnik.getUsername().equals(usernameField.getText())).collect(Collectors.toSet()).size()==0){
            DatotekaKorisnika.unesiKorisnika(new Korisnik(usernameField.getText(),passwordField.getText(), RazinaOvlasti.USER));
        }
    }
}
