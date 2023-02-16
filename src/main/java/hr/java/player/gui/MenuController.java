package hr.java.player.gui;

import hr.java.player.entiteti.RazinaOvlasti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MenuController {
    @FXML
    private MenuItem registracijaMenuItem, odjavaMenuItem, prijavaMenuItem;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Menu administracijaMenu, volumeMenu, radioMenu, playbackMenu;
    @FXML
    void initialize(){
        if (GlavnaAplikacija.isLoggedIn()){
            prijavaMenuItem.setVisible(false);
            volumeSlider.lookupAll(".tick-label").forEach(label -> label.setStyle("-fx-font-size: 50px;"));
            volumeSlider.setValue(GlavnaAplikacija.getCurrentVolume()*100);
            volumeSlider.valueProperty().addListener(((observableValue, oldValue, newValue) -> GlavnaAplikacija.changeVolume((Double) newValue)));
            registracijaMenuItem.setText("Promjeni");
            if (GlavnaAplikacija.getKorisnik().getRazinaOvlasti()== RazinaOvlasti.USER){
                administracijaMenu.setVisible(false);
            }
        }else{
            administracijaMenu.setVisible(false);
            volumeMenu.setVisible(false);
            radioMenu.setVisible(false);
            playbackMenu.setVisible(false);
            odjavaMenuItem.setVisible(false);
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
