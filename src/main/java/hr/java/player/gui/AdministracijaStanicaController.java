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
import java.util.Optional;
import java.util.stream.Collectors;

public final class AdministracijaStanicaController implements Administrirajuci {
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesna pretraga!");
            alert.setHeaderText("Nije moguce pretraziti stanice");
            alert.setContentText("Za bitrate je potrebno unjeti broj ili polje ostaviti prazno!");
            alert.showAndWait();
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
            if (BazaPodataka.dohvatiStanice(null,"","","",null,"",url).isEmpty()){
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjesan unos");
                    alert.setHeaderText("Stanica je uspjesno unesena");
                    alert.setContentText("Stanica "+novaStanica.getName()+" je uspjesno unesena!");
                    alert.showAndWait();
                    clearFields();
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Neuspjesan unos!");
                    alert.setHeaderText("Nije moguce unjeti stanicu");
                    alert.setContentText("Za bitrate je potrebno unjeti broj!");
                    alert.showAndWait();
                }catch (BazaPodatakaException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska");
                    alert.setHeaderText("Dogodila se greska pri radu s bazom");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    Logging.logger.error(e.getMessage(),e);
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Stanica vec postoji");
                alert.setHeaderText("Nije moguce unjeti stanicu u bazu");
                alert.setContentText("Stanica se vec nalazi u bazi te ju nije moguce unjeti.");
                alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesan unos");
            alert.setHeaderText("Pogreska prilikom unosa stanice.");
            alert.setContentText("Potrebno je popuniti sva polja!\n Niste unjeli: "+praznaPolja+"\n Jedino opcionalno polje su zanrovi.");
            alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Promjena stanice");
            alert.setHeaderText("Zelite li promjeniti stanicu?");
            alert.setContentText("Jeste li sigurni da zelite promjeniti stanicu "+odabranaStanica.getName()+" ?");
            ButtonType daButton = new ButtonType("Da");
            ButtonType neButton = new ButtonType("Ne");
            alert.getButtonTypes().setAll(daButton, neButton);
            Optional<ButtonType> odabraniButton = alert.showAndWait();
            if (odabraniButton.get()==daButton) {
                try {
                    if (!bitrateText.isEmpty()) {
                        bitrate = Integer.parseInt(bitrateText);
                    }
                    BazaPodataka.promjeniStanicu(odabranaStanica, naziv, zemlja, codec, bitrate, zanr, url);
                    initialize();
                    clearFields();
                } catch (NumberFormatException e) {
                    Alert alertN = new Alert(Alert.AlertType.INFORMATION);
                    alertN.setTitle("Neuspjesna promjena!");
                    alertN.setHeaderText("Nije moguce promjeniti stanicu");
                    alertN.setContentText("Potrebno je za bitrate unjeti broj ili polje ostaviti prazno!");
                    alertN.showAndWait();
                    Logging.logger.error(e.getMessage(), e);
                } catch (BazaPodatakaException e) {
                    Alert alertB = new Alert(Alert.AlertType.ERROR);
                    alertB.setTitle("Greska");
                    alertB.setHeaderText("Dogodila se greska pri radu s bazom");
                    alertB.setContentText(e.getMessage());
                    alertB.showAndWait();
                    Logging.logger.error(e.getMessage(), e);
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesna promjena!");
            alert.setHeaderText("Odaberite stanicu");
            alert.setContentText("Potrebno je odabrati barem jednu stanicu koju zelite promjeniti!");
            alert.showAndWait();
            Logging.logger.info("Pokusaj promjene stanice bez odabira stanice u tabilici");
        }
    }
    @FXML
    void obrisi(){
        Station odabranaStanica = stationTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje stanice");
            alert.setHeaderText("Zelite li obrisati stanicu?");
            alert.setContentText("Jeste li sigurni da zelite obrisati stanicu "+odabranaStanica.getName()+" ?");
            ButtonType daButton = new ButtonType("Da");
            ButtonType neButton = new ButtonType("Ne");
            alert.getButtonTypes().setAll(daButton, neButton);
            Optional<ButtonType> odabraniButton = alert.showAndWait();
            if (odabraniButton.get()==daButton) {
                try {
                    BazaPodataka.obrisiStanicu(odabranaStanica);
                } catch (BazaPodatakaException e) {
                    Alert alertB = new Alert(Alert.AlertType.ERROR);
                    alertB.setTitle("Greska");
                    alertB.setHeaderText("Dogodila se greska pri radu s bazom");
                    alertB.setContentText(e.getMessage());
                    alertB.showAndWait();
                    Logging.logger.error(e.getMessage(), e);
                }
                initialize();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesno brisanje");
            alert.setHeaderText("Nije moguce obrisati stanicu");
            alert.setContentText("Potrebno je odabrati stanicu koju zelite obrisati!");
            alert.showAndWait();
            Logging.logger.info("Pokusaj brisanja stanice bez odabira stanice u tabilici");
        }
    }
}
