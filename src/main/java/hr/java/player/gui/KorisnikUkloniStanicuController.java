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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KorisnikUkloniStanicuController {
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
            radioTableView.setItems(FXCollections.observableArrayList(korisnikoveStanice.stream()
                    .filter(station -> naziv.isEmpty() || station.getName().toLowerCase().contains(naziv.toLowerCase()))
                    .filter(station -> zanr.isEmpty() || station.getTags().toLowerCase().contains(zanr.toLowerCase()))
                    .filter(station -> zemlja.isEmpty() || station.getCountry().toLowerCase().contains(zemlja.toLowerCase()))
                    .filter(station -> codec.isEmpty() || station.getCodec().toLowerCase().contains(codec.toLowerCase()))
                    .filter(station -> bitrate==null || station.getBitrate().equals(bitrate))
                    .collect(Collectors.toList())));
        }catch (NumberFormatException ex){
            //TODO: alert umjesto sout
            System.out.println("Potrebno je unjeti broj!");
            ex.printStackTrace();
        }
    }
    @FXML
    void obrisi(){
        Station odabranaStanica = radioTableView.getSelectionModel().getSelectedItem();
        if (odabranaStanica!=null){
            BazaPodataka.obrisiStanicuKorisniku(GlavnaAplikacija.getKorisnik().getId(), odabranaStanica);
            initialize();
        }else{
            //TODO: alert umjesto sout
            System.out.println("Potrebno je odabrati barem jednu stanicu");
        }
    }
}
