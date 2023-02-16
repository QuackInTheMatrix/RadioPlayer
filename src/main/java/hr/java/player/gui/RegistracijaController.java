package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.BaseUser;
import hr.java.player.entiteti.RazinaOvlasti;
import hr.java.player.iznimke.BazaPodatakaException;
import hr.java.player.iznimke.DatotekaException;
import hr.java.player.util.DatotekaKorisnika;
import hr.java.player.util.Logging;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegistracijaController implements Alertable{
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
        Map<String,Integer> sviKorisnici = null;
        try {
            sviKorisnici = DatotekaKorisnika.dohvatiSve();
        } catch (DatotekaException e) {
            createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
            Logging.logger.error(e.getMessage(),e);
        }
        if (!imeField.getText().isEmpty() && !prezimeField.getText().isEmpty() && !emailField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && passwordField.getText().equals(ponovljeniPasswordField.getText()) && !sviKorisnici.containsKey(usernameField.getText())){
            try {
                DatotekaKorisnika.unesiKorisnika(new BaseUser(usernameField.getText(),passwordField.getText().hashCode()));
                BazaPodataka.unesiKorisnika(usernameField.getText(),emailField.getText(),imeField.getText(),prezimeField.getText(),passwordField.getText().hashCode(),RazinaOvlasti.USER);
                createAlert("Uspjesna registracija", "Uspjesna registracija novog korisnika", "Korisnik "+usernameField.getText()+" je uspjesno unesen u bazu i datoteku", Alert.AlertType.INFORMATION);
            } catch (BazaPodatakaException e) {
                createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                Logging.logger.error(e.getMessage(),e);
            }catch (DatotekaException e){
                createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
                Logging.logger.error(e.getMessage(),e);
            }
        }else{
            if (sviKorisnici.containsKey(usernameField.getText())){
                createAlert("Neuspjesan unos", "Dogodila se greska pri radu unosu korisnika", "Korisnik sa tim usernameom vec postoji", Alert.AlertType.INFORMATION);
                Logging.logger.info("Pokusaj registracije korisnika sa usernameom koji vec postoji");
            }else{
                List<String> nePopunjenaPolja = new ArrayList<>();
                if (usernameField.getText().isEmpty()){
                    nePopunjenaPolja.add("username");
                }
                if (passwordField.getText().isEmpty()){
                    nePopunjenaPolja.add("password");
                }
                if (imeField.getText().isEmpty()){
                    nePopunjenaPolja.add("ime");
                }
                if (prezimeField.getText().isEmpty()){
                    nePopunjenaPolja.add("prezime");
                }
                if (emailField.getText().isEmpty()){
                    nePopunjenaPolja.add("email");
                }
                if (nePopunjenaPolja.isEmpty()){
                    createAlert("Neuspjesna registracija", "Pogreska prilikom registracije korisnika", "Potrebno je unjeti podudarajuce passworde", Alert.AlertType.INFORMATION);
                }else {
                    String praznaPolja = String.join(", ", nePopunjenaPolja);
                    createAlert("Neuspjesna registracija", "Pogreska prilikom registracije korisnika", "Potrebno je popuniti sva polja!\n Niste unjeli: " + praznaPolja, Alert.AlertType.INFORMATION);
                }
            }
        }
    }
}
