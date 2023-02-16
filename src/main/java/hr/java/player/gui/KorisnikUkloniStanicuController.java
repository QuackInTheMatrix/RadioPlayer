package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.baza.BazaPodataka;
import hr.java.player.iznimke.BazaPodatakaException;
import hr.java.player.util.Logging;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class KorisnikUkloniStanicuController implements Pretrazljiv, Alertable{
    @FXML
    private TextField nazivField, zanrField, zemljaField, codecField, bitrateField;
    @FXML
    private TableView<Station> radioTableView;
    @FXML
    private TableColumn<Station, String> nazivColumn, zanrColumn, zemljaColumn, codecColumn;
    @FXML
    private TableColumn<Station, Integer> bitrateColumn;
    private List<Station> korisnikoveStanice = new ArrayList<>();
    @FXML
    void initialize(){
        korisnikoveStanice = BazaPodataka.dohvatiKorisnikoveStanice(GlavnaAplikacija.getKorisnik().getId(),null);
        nazivColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        zanrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTags()));
        zemljaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        codecColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodec()));
        bitrateColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBitrate()).asObject());
        radioTableView.setItems(FXCollections.observableArrayList(korisnikoveStanice));
    }
    @FXML
    void pretrazi() {
        String naziv = nazivField.getText();
        String zanr = zanrField.getText();
        String zemlja = zemljaField.getText();
        String codec = codecField.getText();
        Integer bitrate;
        try{
            if (!bitrateField.getText().isEmpty()){
                bitrate = Integer.parseInt(bitrateField.getText());
            }else{
                bitrate = null;
            }
            radioTableView.setItems(FXCollections.observableArrayList(filtrirajStanice(korisnikoveStanice,naziv,zanr,zemlja,codec,bitrate)));
        }catch (NumberFormatException e){
            createAlert("Neuspjesna pretraga!", "Nije moguce pretraziti stanice", "Za bitrate je potrebno unjeti broj ili ostaviti prazno!", Alert.AlertType.INFORMATION);
            Logging.logger.error(e.getMessage(),e);
        }
    }
    @FXML
    void obrisi(){
        Station odabranaStanica = radioTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            if (createAlertWithResponse("Brisanje stanice","Zelite li obrisati stanicu?","Jeste li sigurni da zelite obrisati stanicu "+odabranaStanica.getName()+" ?")) {
                try {
                    BazaPodataka.obrisiStanicuKorisniku(GlavnaAplikacija.getKorisnik().getId(), odabranaStanica);
                    initialize();
                } catch (BazaPodatakaException e) {
                    createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                    Logging.logger.error(e.getMessage(),e);
                }
                initialize();
            }
        }else{
            createAlert("Neuspjesno brisanje", "Nije moguce obrisati stanicu", "Potrebno je odabrati stanicu koju zelite obrisati!", Alert.AlertType.INFORMATION);
            Logging.logger.info("Pokusaj brisanja stanice bez odabira stanice u tabilici");
        }
    }
}
