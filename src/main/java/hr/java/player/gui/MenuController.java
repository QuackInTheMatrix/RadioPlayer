package hr.java.player.gui;

import hr.java.player.entiteti.RazinaOvlasti;
import hr.java.player.util.Logging;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MenuController {
    @FXML
    private MenuItem registracijaMenuItem;
    @FXML
    private Menu administracijaMenu;
    @FXML
    void initialize(){
        if (GlavnaAplikacija.isLoggedIn()){
            registracijaMenuItem.setText("Promjeni");
            if (GlavnaAplikacija.getKorisnik().getRazinaOvlasti()== RazinaOvlasti.USER){
                administracijaMenu.setVisible(false);
            }
        }else{
            administracijaMenu.setVisible(false);
        }
    }
    @FXML
    void promjeniEkran(ActionEvent event) {
        try {
            String imeDatoteke;
            MenuItem kliknuti = (MenuItem) event.getSource();
            switch (kliknuti.getText()){
                case "Registracija" -> imeDatoteke="registracija";
                case "Odjava" -> {odjaviKorisnika(); imeDatoteke="prijava";}
                case "Slusaj" -> imeDatoteke="slusanje";
                case "Dodaj" -> imeDatoteke="dodavanjeStanica";
                case "Ukloni" -> imeDatoteke="korisnikUkloniStanicu";
                case "Promjeni" -> imeDatoteke="promjeniKorisnika";
                case "Korisnici" -> imeDatoteke="administracijaKorisnika";
                case "Stanice" -> imeDatoteke="administracijaStanica";
                case "Pregled" -> imeDatoteke="pregledPromjena";
                default -> imeDatoteke="prijava";
            }
            if (!(imeDatoteke.equals("prijava") || imeDatoteke.equals("registracija")) && !GlavnaAplikacija.isLoggedIn()){
                imeDatoteke="prijava";
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nedozvoljen pristup");
                alert.setHeaderText("Potrebna prijava");
                alert.setContentText("Kako bi ste pristupili "+kliknuti.getText()+" prvo se potrebno prijaviti");
                alert.showAndWait();
                Logging.logger.info("Pokusaj brisanja stanice bez odabira stanice u tabilici");
            }
            BorderPane root = FXMLLoader.load(getClass().getResource(imeDatoteke+".fxml"));
            GlavnaAplikacija.setNewStage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void odjaviKorisnika(){
        GlavnaAplikacija.odjaviKorisnika();
    }
    @FXML
    void playbackControl(ActionEvent event){
        switch (((MenuItem)event.getSource()).getText()){
            case "Play" -> GlavnaAplikacija.PlayMedia();
            case "Pauziraj" -> GlavnaAplikacija.pauseMedia();
        }
    }
}
