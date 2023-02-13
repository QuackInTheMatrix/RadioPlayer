package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.SearchMode;
import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.baza.BazaPodataka;
import hr.java.player.iznimke.BazaPodatakaException;
import hr.java.player.util.Logging;
import hr.java.player.util.RadioStations;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class DodavanjeStanicaController implements Pretrazljiv{
    @FXML
    private ChoiceBox<String> tipPretrageChoiceBox;
    @FXML
    private TextField glavnaPretragaField, nazivField, zanrField, zemljaField, codecField, bitrateField;
    @FXML
    private TableView<Station> radioTableView;
    @FXML
    private TableColumn<Station, String> nazivColumn, zanrColumn, zemljaColumn, codecColumn;
    @FXML
    private TableColumn<Station, Integer> bitrateColumn;
    @FXML
    void initialize(){
        List<Station> stanice = RadioStations.dohvatiStanice();
        nazivColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        zanrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTags()));
        zemljaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        codecColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodec()));
        bitrateColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBitrate()).asObject());
        radioTableView.setItems(FXCollections.observableArrayList(stanice));
    }
    @FXML
    void pretrazi() {
        String glavnaPretraga = glavnaPretragaField.getText();
        String naziv = nazivField.getText();
        String zanr = zanrField.getText();
        String zemlja = zemljaField.getText();
        String codec = codecField.getText();
        Integer bitrate = null;
        SearchMode mode;
        switch (tipPretrageChoiceBox.getSelectionModel().getSelectedItem()){
            case "Naziv" -> mode = SearchMode.BYNAME;
            case "Zanr" -> mode = SearchMode.BYTAG;
            case "Zemlja" -> mode = SearchMode.BYCOUNTRY;
            case "Codec" -> mode = SearchMode.BYCODEC;
            default -> mode = SearchMode.BYNAME;
        }
        try{
            if (!bitrateField.getText().isEmpty()){
                bitrate = Integer.parseInt(bitrateField.getText());
            }
            if (glavnaPretraga.isEmpty()){
                if (!naziv.isEmpty() || !zanr.isEmpty() || !zemlja.isEmpty() || !codec.isEmpty() || bitrate!=null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Neuspjesna pretraga");
                    alert.setHeaderText("Nije moguce pretraziti stanice");
                    alert.setContentText("Potrebno je odabrati glavni nacin pretrage iz choiceboxa i unjeti zeljenju vrjednost");
                    alert.showAndWait();
                }else{
                    radioTableView.setItems(FXCollections.observableArrayList(RadioStations.dohvatiStanice()));
                }
            }else {
                radioTableView.setItems(FXCollections.observableArrayList(filtrirajStanice(RadioStations.dohvatiStanice(mode, glavnaPretraga),naziv,zanr,zemlja,codec,bitrate)));
            }
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesna pretraga!");
            alert.setHeaderText("Nije moguce pretraziti stanice");
            alert.setContentText("Za bitrate je potrebno unjeti broj ili polje ostaviti prazno!");
            alert.showAndWait();
            Logging.logger.error(e.getMessage(),e);
        }
    }
    @FXML
    void dodaj(){
        Station odabranaStanica = radioTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            try {
                BazaPodataka.unesiStanicu(GlavnaAplikacija.getKorisnik().getId(), odabranaStanica);
            } catch (BazaPodatakaException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greska");
                alert.setHeaderText("Dogodila se greska pri radu s bazom");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Logging.logger.error(e.getMessage(),e);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesno dodavanje");
            alert.setHeaderText("Potrebno je odabrati stanicu");
            alert.setContentText("Kako bi se stanica mogla dodati potrebno ju je odabrati u tablici!");
            alert.showAndWait();
            Logging.logger.info("Pokusaj dodavanja stanice bez odabira u tablici");
        }
    }
    @FXML
    void isprobaj(){
        Station odabranaStanica=radioTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            GlavnaAplikacija.playMedia(odabranaStanica.getUrl());
            System.out.println(odabranaStanica.getUrl());
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesno");
            alert.setHeaderText("Potrebno je odabrati stanicu");
            alert.setContentText("Kako bi se stanica mogla pustiti potrebno ju je odabrati u tablici!");
            alert.showAndWait();
        }
    }
}
