package hr.java.player.gui;

import hr.java.player.entiteti.Korisnik;
import hr.java.player.util.DatotekaKorisnika;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Set;
import java.util.stream.Collectors;

public class PrijavaController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    void prijava(){
        Set<Korisnik> sviKorisnici = DatotekaKorisnika.dohvatiSve();
        if (sviKorisnici.stream().filter(korisnik -> korisnik.getUsername().equals(usernameField.getText())).filter(korisnik -> korisnik.getPassword().equals(passwordField.getText())).collect(Collectors.toSet()).size()==1){
            System.out.println("Uspjesna prijava");
        }
    }
}
