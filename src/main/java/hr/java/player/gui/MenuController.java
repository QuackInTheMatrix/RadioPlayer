package hr.java.player.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MenuController {
    @FXML
    private MenuItem registracijaMenuItem;
    @FXML
    void initialize(){
        if (GlavnaAplikacija.isLoggedIn()){
            registracijaMenuItem.setText("Promjeni");
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
                default -> imeDatoteke="prijava";
            }
            if (!(imeDatoteke.equals("prijava") || imeDatoteke.equals("registracija")) && !GlavnaAplikacija.isLoggedIn()){
                imeDatoteke="prijava";
                //TODO: zamjeniti sout sa alertom
                System.out.println("Prvo se potrebno prijaviti/registrirati kako bi koristili aplikaciju!");
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
