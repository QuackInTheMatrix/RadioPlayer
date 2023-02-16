package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.SearchMode;
import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.Stanica;
import hr.java.player.util.Logging;
import hr.java.player.util.RadioStations;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class DodavanjeStanicaController implements Pretrazljiv, Alertable{
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
                    createAlert("Neuspjesna pretraga", "Nije moguce pretraziti stanice", "Potrebno je odabrati glavni nacin pretrage iz choiceboxa i unjeti zeljenju vrjednost", Alert.AlertType.INFORMATION);
                }else{
                    radioTableView.setItems(FXCollections.observableArrayList(RadioStations.dohvatiStanice()));
                }
            }else {
                radioTableView.setItems(FXCollections.observableArrayList(filtrirajStanice(RadioStations.dohvatiStanice(mode, glavnaPretraga),naziv,zanr,zemlja,codec,bitrate)));
            }
        }catch (NumberFormatException e){
            createAlert("Neuspjesna pretraga!", "Nije moguce pretraziti stanice", "Za bitrate je potrebno unjeti broj ili ostaviti prazno!", Alert.AlertType.INFORMATION);
            Logging.logger.error(e.getMessage(),e);
        }
    }
    @FXML
    void dodaj(){
        Station odabranaStanica = radioTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            if (BazaPodataka.stanicaExists(odabranaStanica.getUrl())){
                Stanica odabrana = BazaPodataka.dohvatiStanice(null,"","","",null,"", odabranaStanica.getUrl()).get(0);
                if (!BazaPodataka.dohvatiKorisnikoveStanice(GlavnaAplikacija.getKorisnik().getId(), odabrana.getId()).isEmpty()){
                    createAlert("Postojeca stanica", "Stanica je vec u favoritima",  "Stanica se vec nalazi na stranci slusaj pod favoritima.", Alert.AlertType.INFORMATION);
                }else{
                    BazaPodataka.unesiStanicu(GlavnaAplikacija.getKorisnik().getId(), odabranaStanica);
                    createAlert("Uspjesno dodavanje", "Stanica dodana u favorite", "Stanica "+odabranaStanica.getName()+" uspjesno je dodana u favorite", Alert.AlertType.INFORMATION);
                    Logging.logger.info("Stanica "+odabranaStanica.getName()+" dodana je u bazu");
                }
            }else{
                BazaPodataka.unesiStanicu(GlavnaAplikacija.getKorisnik().getId(), odabranaStanica);
                createAlert("Uspjesno dodavanje", "Stanica dodana u favorite", "Stanica "+odabranaStanica.getName()+" uspjesno je dodana u favorite", Alert.AlertType.INFORMATION);
                Logging.logger.info("Stanica "+odabranaStanica.getName()+" dodana je u bazu");
            }
        }else{
            createAlert("Neuspjesno dodavanje", "Potrebno je odabrati stanicu", "Kako bi se stanica mogla dodati potrebno ju je odabrati u tablici!", Alert.AlertType.INFORMATION);
            Logging.logger.info("Pokusaj dodavanja stanice bez odabira u tablici");
        }
    }
    @FXML
    void isprobaj(){
        Station odabranaStanica=radioTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            GlavnaAplikacija.playMedia(odabranaStanica.getUrl());
        }else{
            createAlert("Pogreska", "Potrebno je odabrati stanicu", "Kako bi se stanica mogla pustiti potrebno ju je odabrati u tablici!", Alert.AlertType.INFORMATION);
        }
    }
}
