package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.baza.BazaPodataka;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.List;

public class SlusanjeController {
    @FXML
    private Label trenutnaPjesmaLabel, trenutniRadioLabel, trenutnaZemljaLabel, trenutniJezikLabel, trenutniBitrateLabel;
    @FXML
    private Slider volumeSlider;

    @FXML
    private MediaView mediaView;

    @FXML
    private TableView<Station> radioTableView;
    @FXML
    private TableColumn<Station, String> nazivColumn, zanrColumn, zemljaColumn, codecColumn;
    @FXML
    private TableColumn<Station, Integer> bitrateColumn;

    @FXML
    void initialize() {
        volumeSlider.valueProperty().addListener(((observableValue, oldValue, newValue) -> GlavnaAplikacija.changeVolume((Double) newValue)));
        List<Station> korisnikoveStanice = BazaPodataka.dohvatiKorisnikoveStanice(GlavnaAplikacija.getKorisnik().getId(),null);
        nazivColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        zanrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTags()));
        zemljaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        codecColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodec()));
        bitrateColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBitrate()).asObject());
        radioTableView.setItems(FXCollections.observableArrayList(korisnikoveStanice));
        updateInfo();
    }
    void updateInfo(){
        if (GlavnaAplikacija.getStatus().equals(MediaPlayer.Status.PLAYING)){
            System.out.println(GlavnaAplikacija.getMedia().getMetadata());
            System.out.println(GlavnaAplikacija.getMedia().getTracks());
        }else{
            //TODO: reset everything to empty
        }
    }
    @FXML
    void pokreniRadio(){
        Station odabranaStanica=radioTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            GlavnaAplikacija.playMedia(odabranaStanica.getUrl());
            mediaView.setMediaPlayer(GlavnaAplikacija.getMediaPlayer());
            updateInfo();
        }else{
            //TODO: napraviti alert
            System.out.println("Potrebno je odabrati stanicu!");
        }
    }

    public void pauzirajRadio(){
        GlavnaAplikacija.pauseMedia();
    }
}
