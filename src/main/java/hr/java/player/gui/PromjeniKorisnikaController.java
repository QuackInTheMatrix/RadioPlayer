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
import javafx.scene.control.TextField;

public class PromjeniKorisnikaController implements Alertable{
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
        if (BazaPodataka.usernameExists(username) && !username.equals(korisnik.getUsername())){
                createAlert("Neuspjesna promjena", "Neuspjesna promjena korisnika", "Potrebno je odabrati drugacije korisnicko ime jer je "+username+" zauzeto.", Alert.AlertType.INFORMATION);
                Logging.logger.info("Pokusaj promjene korisnika na vec zauzeto korisnicko ime");
        }else{
            if (createAlertWithResponse("Promjena korisnika", "Zelite li promjeniti korisnika?", "Jeste li sigurni da zelite promjeniti korisnika "+korisnik.getUsername()+" ?")) {
                try {
                    BazaPodataka.promjeniKorisnika(korisnik.getId(), username, email, ime, prezime, passwordHash, null);
                } catch (BazaPodatakaException e) {
                    createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                    Logging.logger.error(e.getMessage(), e);
                }
                if (!password.isEmpty() && !passwordHash.equals(korisnik.getPasswordHash())) {
                    try {
                        DatotekaKorisnika.promjeniPassword(new BaseUser(korisnik.getUsername(), passwordHash));
                    } catch (DatotekaException e) {
                        createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
                        Logging.logger.error(e.getMessage(), e);
                    }
                }
                if (!username.isEmpty() && !username.equals(korisnik.getUsername())) {
                    try {
                        DatotekaKorisnika.promjeniUsername(korisnik.getUsername(), username);
                    } catch (DatotekaException e) {
                        createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
                        Logging.logger.error(e.getMessage(), e);
                    }
                }
                korisnik = BazaPodataka.dohvatiKorisnike(korisnik.getId(), "","","","",null,null).get(0);
                GlavnaAplikacija.prijaviKorisnika(korisnik.getUsername());
            }
        }
    }
}
