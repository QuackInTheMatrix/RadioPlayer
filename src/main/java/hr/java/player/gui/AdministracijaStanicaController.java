package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.baza.BazaPodataka;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class AdministracijaStanicaController {
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
        }catch (NumberFormatException ex){
            //TODO: alert umjesto sout
            System.out.println("Potrebno je unjeti broj!");
            ex.printStackTrace();
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
                } catch (NumberFormatException ex) {
                    //TODO: alert umjesto sout
                    System.out.println("Potrebno je unjeti broj!");
                    ex.printStackTrace();
                }
            }else{
                //TODO: alert umjesto sout
                System.out.println("Stanica sa tim urlom vec postoji u bazi");
            }
        }else{
            //TODO: alert umjesto sout
            System.out.println("Jedino opcionalno polje su zanrovi!");
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
            try {
                if (!bitrateText.isEmpty()){
                    bitrate = Integer.parseInt(bitrateText);
                }
                BazaPodataka.promjeniStanicu(odabranaStanica, naziv,zemlja,codec,bitrate,zanr,url);
                initialize();
            } catch (NumberFormatException ex) {
                //TODO: alert umjesto sout
                System.out.println("Potrebno je unjeti broj!");
                ex.printStackTrace();
            }
        }else{
            //TODO: alert umjesto sout
            System.out.println("Potrebno je odabrati barem jednu stanicu!");
        }
    }
    @FXML
    void obrisi(){
        Station odabranaStanica = stationTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            BazaPodataka.obrisiStanicu(odabranaStanica);
            initialize();
        }else{
            //TODO: alert umjesto sout
            System.out.println("Potrebno je odabrati stanicu koju zelite obrisati!");
        }
    }
}
