package hr.java.player.gui;

import hr.java.player.entiteti.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class PregledPromjenaController {
    @FXML
    private TextField usernameField, nazivTabliceField;
    @FXML
    private ChoiceBox<VrstaPromjene> vrstaPromjeneChoiceBox;
    @FXML
    private ChoiceBox<RazinaOvlasti> razinaOvlastiChoiceBox;
    @FXML
    private DatePicker datumPromjenePicker;
    @FXML
    private TableView<Promjena<String, Entitet>> promjenaTableView;
    @FXML
    private TableColumn<Promjena<String, Entitet>,String> izvrsiteljColumn, razinaOvlastiColumn, akcijaColumn, stareVrjednostiColumn, noveVrjednostiColumn, tablicaColumn, vrijemeColumn;
    private Promjene svePromjene;
    @FXML
    void initialize(){
        svePromjene=GlavnaAplikacija.getSvePromjene();
        izvrsiteljColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVrsiteljPromjena().getUsername()));
        razinaOvlastiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVrsiteljPromjena().getRazinaOvlasti().getNaziv()));
        akcijaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVrstaPromjene().getNaziv()));
        stareVrjednostiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPromjeniPodataci().stream().map(promjenjenPodatak -> promjenjenPodatak.getStaraVrjednost()).collect(Collectors.joining("\n"))));
        noveVrjednostiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPromjeniPodataci().stream().map(promjenjenPodatak -> promjenjenPodatak.getNovaVrjednost()).collect(Collectors.joining("\n"))));
        tablicaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipPromjenjenog()));
        vrijemeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVrijemePromjene().toString()));
        promjenaTableView.setItems(FXCollections.observableArrayList(svePromjene.getList()));
        razinaOvlastiChoiceBox.setItems(FXCollections.observableArrayList(RazinaOvlasti.values()));
        razinaOvlastiChoiceBox.getItems().add(null);
        vrstaPromjeneChoiceBox.setItems(FXCollections.observableArrayList(VrstaPromjene.values()));
        vrstaPromjeneChoiceBox.getItems().add(null);
    }
    @FXML
    void pretrazi(){
        String username = usernameField.getText();
        String nazivTablice = nazivTabliceField.getText();
        VrstaPromjene vrstaPromjene = vrstaPromjeneChoiceBox.getSelectionModel().getSelectedItem();
        RazinaOvlasti razinaOvlasti = razinaOvlastiChoiceBox.getSelectionModel().getSelectedItem();
        LocalDate datumPromjene = datumPromjenePicker.getValue();
        promjenaTableView.setItems(FXCollections.observableArrayList(svePromjene.getList().stream()
                .filter(promjena -> username.isEmpty() || promjena.getVrsiteljPromjena().getUsername().equals(username))
                .filter(promjena -> nazivTablice.isEmpty() || promjena.getTipPromjenjenog().contains(nazivTablice))
                .filter(promjena -> vrstaPromjene==null || promjena.getVrstaPromjene().equals(vrstaPromjene))
                .filter(promjena -> razinaOvlasti==null || promjena.getVrsiteljPromjena().getRazinaOvlasti().equals(razinaOvlasti))
                .filter(promjena -> datumPromjene==null || promjena.getVrijemePromjene().toLocalDate().equals(datumPromjene))
                .collect(Collectors.toList())));
    }
}
