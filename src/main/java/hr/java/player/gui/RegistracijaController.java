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
        Map<String,Integer> sviKorisnici = null;
        try {
            sviKorisnici = DatotekaKorisnika.dohvatiSve();
        } catch (DatotekaException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska pri radu sa datotekom");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Logging.logger.error(e.getMessage(),e);
        }
        if (!imeField.getText().isEmpty() && !prezimeField.getText().isEmpty() && !emailField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && passwordField.getText().equals(ponovljeniPasswordField.getText()) && !sviKorisnici.containsKey(usernameField.getText())){

            try {
                DatotekaKorisnika.unesiKorisnika(new BaseUser(usernameField.getText(),passwordField.getText().hashCode()));
                BazaPodataka.unesiKorisnika(usernameField.getText(),emailField.getText(),imeField.getText(),prezimeField.getText(),passwordField.getText().hashCode(),RazinaOvlasti.USER);
            } catch (BazaPodatakaException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greska");
                alert.setHeaderText("Dogodila se greska pri radu s bazom");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Logging.logger.error(e.getMessage(),e);
            }catch (DatotekaException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greska");
                alert.setHeaderText("Dogodila se greska pri radu sa datotekom");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Logging.logger.error(e.getMessage(),e);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjesna registracija");
            alert.setHeaderText("Uspjesna registracija novog korisnika");
            alert.setContentText("Korisnik "+usernameField.getText()+" je uspjesno unesen u bazu i datoteku");
            alert.showAndWait();
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
            String praznaPolja = String.join(", ",nePopunjenaPolja);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Neuspjesna registracija");
            alert.setHeaderText("Pogreska prilikom registracije korisnika");
            alert.setContentText("Potrebno je popuniti sva polja!\n Niste unjeli: "+praznaPolja);
            alert.showAndWait();
        }
    }
}
