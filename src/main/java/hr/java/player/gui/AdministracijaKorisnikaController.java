package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.BaseUser;
import hr.java.player.entiteti.Korisnik;
import hr.java.player.entiteti.RazinaOvlasti;
import hr.java.player.iznimke.BazaPodatakaException;
import hr.java.player.iznimke.DatotekaException;
import hr.java.player.util.DatotekaKorisnika;
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

public final class AdministracijaKorisnikaController implements Administrirajuci {
    @FXML
    private TextField usernameField, imeField, prezimeField, emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox<RazinaOvlasti> razinaOvlastiChoiceBox;
    @FXML
    private TableView<Korisnik> korisnikTableView;
    @FXML
    private TableColumn<Korisnik,String> usernameColumn, imeColumn, prezimeColumn, emailColumn, ovlastiColumn;
    @FXML
    private TableColumn<Korisnik,Integer> passwordHashColumn;
    private List<Korisnik> korisnici;
    @FXML
    void initialize(){
        korisnici = BazaPodataka.dohvatiKorisnike(null,"","","","",null,null);
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        imeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));
        prezimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        ovlastiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRazinaOvlasti().getNaziv()));
        passwordHashColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPasswordHash()).asObject());
        korisnikTableView.setItems(FXCollections.observableArrayList(korisnici));
        razinaOvlastiChoiceBox.setItems(FXCollections.observableArrayList(RazinaOvlasti.values()));
        razinaOvlastiChoiceBox.getItems().add(null);
        usernameField.clear();
        imeField.clear();
        passwordField.clear();
        prezimeField.clear();
        emailField.clear();
    }
    @FXML
    void unesi(){
        String username = usernameField.getText();
        String passwordText = passwordField.getText();
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String email = emailField.getText();
        RazinaOvlasti razinaOvlasti=razinaOvlastiChoiceBox.getSelectionModel().getSelectedItem();
        if (!username.isEmpty() && !passwordText.isEmpty() && !ime.isEmpty() && !prezime.isEmpty() && !email.isEmpty() && razinaOvlasti!=null){
            if (!BazaPodataka.dohvatiKorisnike(null,username,"","","",null,null).isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Neuspjesna registracija");
                alert.setHeaderText("Pogreska prilikom registracije");
                alert.setContentText("Uneseno korisnicko ime je zauzeto!\n Molimo odaberite drugo.");
                alert.showAndWait();
                Logging.logger.info("Uneseno korisnicko ime je zauzeto");
            }else{
                try {
                    BazaPodataka.unesiKorisnika(username,email,ime,prezime,passwordText.hashCode(),razinaOvlasti);
                    DatotekaKorisnika.unesiKorisnika(new BaseUser(username,passwordText.hashCode()));
                } catch (BazaPodatakaException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska");
                    alert.setHeaderText("Dogodila se greska pri radu s bazom");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    Logging.logger.error(e.getMessage(),e);
                }catch (DatotekaException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska");
                    alert.setHeaderText("Dogodila se greska pri radu sa datotekom");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    Logging.logger.error(e.getMessage(),e);
                }
            }
            initialize();
        }else{
            List<String> nePopunjenaPolja = new ArrayList<>();
            if (username.isEmpty()){
                nePopunjenaPolja.add("username");
            }
            if (passwordText.isEmpty()){
                nePopunjenaPolja.add("password");
            }
            if (ime.isEmpty()){
                nePopunjenaPolja.add("ime");
            }
            if (prezime.isEmpty()){
                nePopunjenaPolja.add("prezime");
            }
            if (email.isEmpty()){
                nePopunjenaPolja.add("email");
            }
            if (razinaOvlasti==null){
                nePopunjenaPolja.add("razina ovlasti");
            }
            String praznaPolja = String.join(", ",nePopunjenaPolja);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesan unos");
            alert.setHeaderText("Pogreska prilikom unosa korisnika");
            alert.setContentText("Potrebno je popuniti sva polja!\n Niste unjeli: "+praznaPolja);
            alert.showAndWait();
        }
    }
    @FXML
    void promjeni(){
        String username = usernameField.getText();
        String passwordText = passwordField.getText();
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String email = emailField.getText();
        RazinaOvlasti razinaOvlasti=razinaOvlastiChoiceBox.getSelectionModel().getSelectedItem();
        Integer passwordHash=null;
        boolean ispravan=true;
        if (!passwordText.isEmpty()){
            passwordHash=passwordText.hashCode();
        }
        if (korisnikTableView.getSelectionModel().getSelectedItem()!=null){
            if (!username.isEmpty()){
                if (!BazaPodataka.dohvatiKorisnike(null,username,"","","",null,null).isEmpty()){
                    ispravan=false;
                    System.out.println("Potrebno je unjeti unikatan novu username");
                }
            }
            if (ispravan) {
                Korisnik odabraniKorisnik = korisnikTableView.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Promjena korisnika");
                alert.setHeaderText("Zelite li promjeniti korisnika?");
                alert.setContentText("Jeste li sigurni da zelite promjeniti korisnika +"+odabraniKorisnik.getUsername());
                ButtonType daButton = new ButtonType("Da");
                ButtonType neButton = new ButtonType("Ne");
                alert.getButtonTypes().setAll(daButton, neButton);
                Optional<ButtonType> odabraniButton = alert.showAndWait();
                if (odabraniButton.get()==daButton) {
                    try {
                        BazaPodataka.promjeniKorisnika(odabraniKorisnik.getId(), username, email, ime, prezime, passwordHash, razinaOvlasti);
                    } catch (BazaPodatakaException e) {
                        Alert alertB = new Alert(Alert.AlertType.ERROR);
                        alertB.setTitle("Greska");
                        alertB.setHeaderText("Dogodila se greska pri radu s bazom");
                        alertB.setContentText(e.getMessage());
                        alertB.showAndWait();
                        Logging.logger.error(e.getMessage(), e);
                    }
                    if (passwordHash != null) {
                        try {
                            DatotekaKorisnika.promjeniPassword(new BaseUser(odabraniKorisnik.getUsername(), passwordHash));
                        } catch (DatotekaException ex) {
                            Alert alertD = new Alert(Alert.AlertType.ERROR);
                            alertD.setTitle("Greska");
                            alertD.setHeaderText("Dogodila se greska pri radu sa datotekom");
                            alertD.setContentText(ex.getMessage());
                            alertD.showAndWait();
                            Logging.logger.error(ex.getMessage(), ex);
                        }
                    }
                    if (!username.isEmpty()) {
                        try {
                            DatotekaKorisnika.promjeniUsername(odabraniKorisnik.getUsername(), username);
                        } catch (DatotekaException ex) {
                            Alert alertDD = new Alert(Alert.AlertType.ERROR);
                            alertDD.setTitle("Greska");
                            alertDD.setHeaderText("Dogodila se greska pri radu sa datotekom");
                            alertDD.setContentText(ex.getMessage());
                            alertDD.showAndWait();
                            Logging.logger.error(ex.getMessage(), ex);
                        }
                    }
                    if (GlavnaAplikacija.getKorisnik().getId().equals(odabraniKorisnik.getId())) {
                        if (!username.isEmpty()) {
                            GlavnaAplikacija.prijaviKorisnika(username);
                        } else {
                            GlavnaAplikacija.prijaviKorisnika(odabraniKorisnik.getUsername());
                        }
                    }
                    korisnikTableView.getSelectionModel().clearSelection();
                    initialize();
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesna promjena");
            alert.setHeaderText("Korisnik nije oznacen");
            alert.setContentText("Potrebno je oznaciti korisnika u tablici koji se zeli promjeniti");
            alert.showAndWait();
        }
    }
    @FXML
    void pretrazi(){
        String username = usernameField.getText();
        String passwordText = passwordField.getText();
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String email = emailField.getText();
        RazinaOvlasti razinaOvlasti=razinaOvlastiChoiceBox.getSelectionModel().getSelectedItem();
        List<Korisnik> filtriraniKorisnici = korisnici.stream()
                    .filter(korisnik -> username.isEmpty() || korisnik.getUsername().contains(username))
                    .filter(korisnik -> passwordText.isEmpty() || korisnik.getPasswordHash().equals(passwordText.hashCode()))
                    .filter(korisnik -> ime.isEmpty() || korisnik.getIme().contains(ime))
                    .filter(korisnik -> prezime.isEmpty() || korisnik.getPrezime().contains(prezime))
                    .filter(korisnik -> email.isEmpty() || korisnik.getEmail().contains(email))
                    .filter(korisnik -> razinaOvlasti == null || korisnik.getRazinaOvlasti() == razinaOvlasti)
                    .collect(Collectors.toList());
        korisnikTableView.setItems(FXCollections.observableArrayList(filtriraniKorisnici));
        korisnikTableView.getSelectionModel().clearSelection();
    }
    @FXML
    void ukloni(){
        String username = usernameField.getText();
        String passwordText = passwordField.getText();
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String email = emailField.getText();
        RazinaOvlasti razinaOvlasti=razinaOvlastiChoiceBox.getSelectionModel().getSelectedItem();
        Integer passwordHash=null;
        if (!passwordText.isEmpty()){
            passwordHash=passwordText.hashCode();
        }
        if (korisnikTableView.getSelectionModel().getSelectedItem()!=null){
            Korisnik odabraniKorisnik = korisnikTableView.getSelectionModel().getSelectedItem();
            if (odabraniKorisnik.getId().equals(GlavnaAplikacija.getKorisnik().getId())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Neuspjesno brisanje");
                alert.setHeaderText("Nije moguce obrisati korisnika");
                alert.setContentText("Nemoguce je obrisati trenutno prijavljenog korisnika!");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Brisanje korisnika");
                alert.setHeaderText("Zelite li obrisati korisnika?");
                alert.setContentText("Jeste li sigurni da zelite obrisati korisnika "+odabraniKorisnik.getUsername());
                ButtonType daButton = new ButtonType("Da");
                ButtonType neButton = new ButtonType("Ne");
                alert.getButtonTypes().setAll(daButton, neButton);
                Optional<ButtonType> odabraniButton = alert.showAndWait();
                if (odabraniButton.get()==daButton) {
                    try {
                        BazaPodataka.obrisiKorisnika(odabraniKorisnik.getId(), "", "", "", "", null, null);
                        DatotekaKorisnika.obrisiKorisnika(odabraniKorisnik.getUsername());
                    } catch (BazaPodatakaException e) {
                        Alert alertB = new Alert(Alert.AlertType.ERROR);
                        alertB.setTitle("Greska");
                        alertB.setHeaderText("Dogodila se greska pri radu s bazom");
                        alertB.setContentText(e.getMessage());
                        alertB.showAndWait();
                        Logging.logger.error(e.getMessage(),e);
                    } catch (DatotekaException ex) {
                        Alert alertD = new Alert(Alert.AlertType.ERROR);
                        alertD.setTitle("Greska");
                        alertD.setHeaderText("Dogodila se greska pri radu sa datotekom");
                        alertD.setContentText(ex.getMessage());
                        alertD.showAndWait();
                        Logging.logger.error(ex.getMessage(),ex);
                    }
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspjesno brisanje");
            alert.setHeaderText("Nije moguce obrisati korisnika");
            alert.setContentText("Potrebno je odabrati korisnika kojeg zelite obrisati!");
            alert.showAndWait();
        }
        initialize();
        korisnikTableView.getSelectionModel().clearSelection();
    }
}
