package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.SearchMode;
import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.baza.BazaPodataka;
import hr.java.player.util.RadioStations;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class DodavanjeStanicaController {
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
                //TODO: napraviti alert da je potrebno upisati glavnu pretragu ukoliko su druga polja popunjena
                radioTableView.setItems(FXCollections.observableArrayList(RadioStations.dohvatiStanice()));
            }else {
                radioTableView.setItems(FXCollections.observableArrayList(RadioStations.dohvatiStanice(mode, glavnaPretraga, naziv, zanr, zemlja, codec,bitrate)));
            }
        }catch (NumberFormatException ex){
            //TODO: alert umjesto sout
            System.out.println("Potrebno je unjeti broj!");
            ex.printStackTrace();
        }
    }
    @FXML
    void dodaj(){
        if (radioTableView.getSelectionModel().getSelectedItem()!=null){
            BazaPodataka.unesiStanicu(radioTableView.getSelectionModel().getSelectedItem());
        }else{
            //TODO: alert umjesto sout
            System.out.println("Potrebno je odabrati barem jedan radio");
        }
    }
}
