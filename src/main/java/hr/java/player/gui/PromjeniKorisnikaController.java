package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.BaseUser;
import hr.java.player.entiteti.Korisnik;
import hr.java.player.iznimke.BazaPodatakaException;
import hr.java.player.iznimke.DatotekaException;
import hr.java.player.util.DatotekaKorisnika;
import hr.java.player.util.Logging;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

public class PromjeniKorisnikaController{
    @FXML
    private TextField usernameField, passwordField, imeField, prezimeField, emailField;
    private Korisnik korisnik;
    @FXML
    void initialize(){
        korisnik = GlavnaAplikacija.getKorisnik();
        usernameField.setText(korisnik.getUsername());
        imeField.setText(korisnik.getIme());
        prezimeField.setText(korisnik.getPrezime());
        emailField.setText(korisnik.getEmail());
    }

    @FXML
    void promjeni(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String email = emailField.getText();
        Integer passwordHash;
        if (password.isEmpty()){
            passwordHash= null;
        }else{
            passwordHash = passwordField.getText().hashCode();
        }
        if (!BazaPodataka.dohvatiKorisnike(null,username,"","","",null,null).isEmpty()){
            if (!username.equals(korisnik.getUsername())){
                username="";
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Neuspjesna promjena");
                alert.setHeaderText("Neuspjesna promjena korisnika");
                alert.setContentText("Potrebno je odabrati drugacije korisnicko ime jer je "+username+" zauzeto.");
                alert.showAndWait();
                Logging.logger.info("Pokusaj promjene korisnika na vec zauzeto korisnicko ime");
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Promjena korisnika");
        alert.setHeaderText("Zelite li promjeniti korisnika?");
        alert.setContentText("Jeste li sigurni da zelite promjeniti korisnika "+korisnik.getUsername()+" ?");
        ButtonType daButton = new ButtonType("Da");
        ButtonType neButton = new ButtonType("Ne");
        alert.getButtonTypes().setAll(daButton, neButton);
        Optional<ButtonType> odabraniButton = alert.showAndWait();
        if (odabraniButton.get()==daButton) {
            try {
                BazaPodataka.promjeniKorisnika(korisnik.getId(), username, email, ime, prezime, passwordHash, null);
            } catch (BazaPodatakaException e) {
                Alert alertB = new Alert(Alert.AlertType.ERROR);
                alertB.setTitle("Greska");
                alertB.setHeaderText("Dogodila se greska pri radu s bazom");
                alertB.setContentText(e.getMessage());
                alertB.showAndWait();
                Logging.logger.error(e.getMessage(), e);
            }
            if (!password.isEmpty() && !passwordHash.equals(korisnik.getPasswordHash())) {
                try {
                    DatotekaKorisnika.promjeniPassword(new BaseUser(korisnik.getUsername(), passwordHash));
                } catch (DatotekaException e) {
                    Alert alertD = new Alert(Alert.AlertType.ERROR);
                    alertD.setTitle("Greska");
                    alertD.setHeaderText("Dogodila se greska pri radu sa datotekom");
                    alertD.setContentText(e.getMessage());
                    alertD.showAndWait();
                    Logging.logger.error(e.getMessage(), e);
                }
            }
            if (!username.isEmpty() && !username.equals(korisnik.getUsername())) {
                try {
                    DatotekaKorisnika.promjeniUsername(korisnik.getUsername(), username);
                } catch (DatotekaException e) {
                    Alert alertDD = new Alert(Alert.AlertType.ERROR);
                    alertDD.setTitle("Greska");
                    alertDD.setHeaderText("Dogodila se greska pri radu sa datotekom");
                    alertDD.setContentText(e.getMessage());
                    alertDD.showAndWait();
                    Logging.logger.error(e.getMessage(), e);
                }
                GlavnaAplikacija.prijaviKorisnika(username);
            } else {
                GlavnaAplikacija.prijaviKorisnika(korisnik.getUsername());
            }
            korisnik = GlavnaAplikacija.getKorisnik();
        }
    }
}
