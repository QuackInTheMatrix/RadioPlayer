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
import java.util.stream.Collectors;

public final class AdministracijaStanicaController implements Administrirajuci, Alertable {
    @FXML
    private TextField nazivField, bitrateField, zanrField, zemljaField, codecField, urlField;
    @FXML
    private TableView<Station> stationTableView;
    @FXML
    private TableColumn<Station,String> nazivColumn, zanroviColumn, zemljaColumn, codecColumn, urlColumn;
    @FXML
    private TableColumn<Station,Integer> bitrateColumn;
    private List<Station> sveStanice;
    @FXML
    void initialize(){
        sveStanice = BazaPodataka.dohvatiStanice(null,"","","",null,"","").stream().map(stanica -> stanica.getStanica()).collect(Collectors.toList());
        nazivColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        zanroviColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTags()));
        zemljaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        codecColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodec()));
        urlColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUrl()));
        bitrateColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBitrate()).asObject());
        stationTableView.setItems(FXCollections.observableArrayList(sveStanice));
    }
    private void clearFields(){
        nazivField.clear();
        zanrField.clear();
        zemljaField.clear();
        codecField.clear();
        bitrateField.clear();
        urlField.clear();
    }
    @FXML
    void pretrazi(){
        String naziv = nazivField.getText();
        String zanr = zanrField.getText();
        String zemlja = zemljaField.getText();
        String codec = codecField.getText();
        String bitrateText = bitrateField.getText();
        String url = urlField.getText();
        Integer bitrate;
        try{
            if (!bitrateText.isEmpty()){
                bitrate = Integer.parseInt(bitrateField.getText());
            }else{
                bitrate=null;
            }
            List<Station> filtriraneStanice = sveStanice.stream()
                    .filter(station -> naziv.isEmpty() || station.getName().toLowerCase().contains(naziv.toLowerCase()))
                    .filter(station -> zanr.isEmpty() || station.getTags().toLowerCase().contains(zanr.toLowerCase()))
                    .filter(station -> zemlja.isEmpty() || station.getCountry().toLowerCase().contains(zemlja.toLowerCase()))
                    .filter(station -> codec.isEmpty() || station.getCodec().toLowerCase().contains(codec.toLowerCase()))
                    .filter(station -> bitrate==null || station.getBitrate().equals(bitrate))
                    .filter(station -> url.isEmpty() || station.getUrl().contains(url))
                    .collect(Collectors.toList());
            stationTableView.setItems(FXCollections.observableArrayList(filtriraneStanice));
            stationTableView.getSelectionModel().clearSelection();
        }catch (NumberFormatException e){
            createAlert("Neuspjesna pretraga!", "Nije moguce pretraziti stanice", "Za bitrate je potrebno unjeti broj ili polje ostaviti prazno!", Alert.AlertType.INFORMATION);
            Logging.logger.error(e.getMessage(),e);
        }
    }
    @FXML
    void unesi(){
        String naziv = nazivField.getText();
        String zanr = zanrField.getText();
        String zemlja = zemljaField.getText();
        String codec = codecField.getText();
        String bitrateText = bitrateField.getText();
        String url = urlField.getText();
        Integer bitrate;
        if (!naziv.isEmpty() && !url.isEmpty() && !bitrateText.isEmpty() && !zemlja.isEmpty() && !codec.isEmpty()) {
            if (!BazaPodataka.stanicaExists(url)){
                try {
                    bitrate = Integer.parseInt(bitrateText);
                    Station novaStanica = new Station();
                    novaStanica.setName(naziv);
                    novaStanica.setTags(zanr);
                    novaStanica.setCountry(zemlja);
                    novaStanica.setCodec(codec);
                    novaStanica.setBitrate(bitrate);
                    novaStanica.setUrl(url);
                    BazaPodataka.unesiStanicu(null, novaStanica);
                    initialize();
                    createAlert("Uspjesan unos", "Stanica je uspjesno unesena", "Stanica "+novaStanica.getName()+" je uspjesno unesena!", Alert.AlertType.INFORMATION);
                    clearFields();
                    initialize();
                } catch (NumberFormatException e) {
                    createAlert("Neuspjesan unos!", "Nije moguce unjeti stanicu", "Za bitrate je potrebno unjeti broj!", Alert.AlertType.INFORMATION);
                    Logging.logger.error(e.getMessage(),e);
                }
            }else{
                createAlert("Stanica vec postoji", "Nije moguce unjeti stanicu u bazu", "Stanica se vec nalazi u bazi te ju nije moguce unjeti.", Alert.AlertType.INFORMATION);
                Logging.logger.info("Stanica se vec nalazi u bazi te ju nije moguce unjeti.");
            }
        }else{
            List<String> nePopunjenaPolja = new ArrayList<>();
            if (naziv.isEmpty()){
                nePopunjenaPolja.add("naziv");
            }
            if (zemlja.isEmpty()){
                nePopunjenaPolja.add("zemlja");
            }
            if (codec.isEmpty()){
                nePopunjenaPolja.add("codec");
            }
            if (bitrateText.isEmpty()){
                nePopunjenaPolja.add("bitrate");
            }

            String praznaPolja = String.join(", ",nePopunjenaPolja);
            createAlert("Neuspjesan unos", "Pogreska prilikom unosa stanice.", "Potrebno je popuniti sva polja!\n Niste unjeli: "+praznaPolja+"\n Jedino opcionalno polje su zanrovi.", Alert.AlertType.INFORMATION);
            Logging.logger.info("Sva potrebna polja nisu popunjena prilikom unosa stanice");
        }
    }
    @FXML
    void promjeni(){
        String naziv = nazivField.getText();
        String zanr = zanrField.getText();
        String zemlja = zemljaField.getText();
        String codec = codecField.getText();
        String bitrateText = bitrateField.getText();
        String url = urlField.getText();
        Integer bitrate=null;
        Station odabranaStanica = stationTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            if (createAlertWithResponse("Promjena stanice", "Zelite li promjeniti stanicu?", "Jeste li sigurni da zelite promjeniti stanicu "+odabranaStanica.getName()+" ?")) {
                try {
                    if (!bitrateText.isEmpty()) {
                        bitrate = Integer.parseInt(bitrateText);
                    }
                    BazaPodataka.promjeniStanicu(odabranaStanica, naziv, zemlja, codec, bitrate, zanr, url);
                    initialize();
                    clearFields();
                } catch (NumberFormatException e) {
                    createAlert("Neuspjesna promjena!", "Nije moguce promjeniti stanicu", "Za bitrate je potrebno unjeti broj ili ostaviti prazno!", Alert.AlertType.INFORMATION);
                    Logging.logger.error(e.getMessage(),e);
                } catch (BazaPodatakaException e) {
                    createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                    Logging.logger.error(e.getMessage(),e);
                }
            }
        }else{
            createAlert("Neuspjesna promjena!", "Odaberite stanicu", "Potrebno je odabrati barem jednu stanicu koju zelite promjeniti!", Alert.AlertType.INFORMATION);
            Logging.logger.info("Pokusaj promjene stanice bez odabira stanice u tabilici");
        }
    }
    @FXML
    void obrisi(){
        Station odabranaStanica = stationTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            if (createAlertWithResponse("Brisanje stanice", "Zelite li obrisati stanicu?", "Jeste li sigurni da zelite obrisati stanicu "+odabranaStanica.getName()+" ?")) {
                try {
                    BazaPodataka.obrisiStanicu(odabranaStanica);
                    initialize();
                } catch (BazaPodatakaException e) {
                    createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                    Logging.logger.error(e.getMessage(),e);
                }
            }
        }else{
            createAlert("Neuspjesno brisanje", "Nije moguce obrisati stanicu", "Potrebno je odabrati stanicu koju zelite obrisati!", Alert.AlertType.INFORMATION);
            Logging.logger.info("Pokusaj brisanja stanice bez odabira stanice u tabilici");
        }
    }
}
