package hr.java.player.gui;

import hr.java.player.entiteti.Korisnik;
import hr.java.player.iznimke.DatotekaException;
import hr.java.player.iznimke.KorisnikException;
import hr.java.player.util.DatotekaKorisnika;
import hr.java.player.util.Logging;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrijavaController implements Alertable{
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
        prijavaGumb.setOnAction(event -> {
            try{
                prijava();
            }catch (DatotekaException e){
                createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
                Logging.logger.error(e.getMessage(),e);
            }
            catch (KorisnikException e){
                createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                Logging.logger.error(e.getMessage(),e);
            }
        });
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
    void prijava()throws DatotekaException, KorisnikException {
        Map<String,Integer> sviKorisnici = new HashMap<>();
        try {
            sviKorisnici = DatotekaKorisnika.dohvatiSve();
        } catch (DatotekaException e) {
            throw new RuntimeException(e);
        }
        if (sviKorisnici.containsKey(usernameField.getText()) && sviKorisnici.get(usernameField.getText())==passwordField.getText().hashCode()){
            GlavnaAplikacija.prijaviKorisnika(usernameField.getText());
            try {
                BorderPane root = FXMLLoader.load(getClass().getResource("prijava.fxml"));
                GlavnaAplikacija.setNewStage(root);
                createAlert("Uspjesna prijava!", "Uspjesno ste se prijavili", "Dobrodosli "+GlavnaAplikacija.getKorisnik().getIme()+".\nRadio mozete poceti slusati klikom na menu Radio->Dodaj gdje mozete isporobati i dodati radio u favorite koji ce se prikazivati na stranici Radio->Slusaj.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                createAlert("Greska", "Dogodila se greska pri radu sa fxml datotekom", e.getMessage(), Alert.AlertType.ERROR);
                Logging.logger.error(e.getMessage(),e);
            }
        }else{
            throw new KorisnikException("Neuspjesna prijava");
        }
    }
}
