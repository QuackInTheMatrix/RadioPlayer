package hr.java.player.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MenuController {
    @FXML
    void promjeniEkran(ActionEvent event) {
        try {
            String imeDatoteke;
            MenuItem kliknuti = (MenuItem) event.getSource();
            switch (kliknuti.getText()){
                case "Registracija" -> imeDatoteke="registracija";
                case "Odjava" -> {odjaviKorisnika(); imeDatoteke="prijava";}
                case "Slusaj" -> imeDatoteke="slusanje";
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
        //TODO: implementirati odjavu korisnika
    }
    @FXML
    void playbackControl(ActionEvent event){
        switch (((MenuItem)event.getSource()).getText()){
            case "Play" -> GlavnaAplikacija.PlayMedia();
            case "Pauziraj" -> GlavnaAplikacija.stopMedia();
        }
    }
}
