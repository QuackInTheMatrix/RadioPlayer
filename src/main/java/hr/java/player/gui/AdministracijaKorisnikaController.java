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
import java.util.stream.Collectors;

public final class AdministracijaKorisnikaController implements Administrirajuci, Alertable {
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
            if (BazaPodataka.usernameExists(username)){
                createAlert("Neuspjesna registracija", "Pogreska prilikom registracije", "Uneseno korisnicko ime je zauzeto!\n Molimo odaberite drugo.", Alert.AlertType.WARNING);
                Logging.logger.info("Uneseno korisnicko ime je zauzeto");
            }else{
                try {
                    BazaPodataka.unesiKorisnika(username,email,ime,prezime,passwordText.hashCode(),razinaOvlasti);
                    DatotekaKorisnika.unesiKorisnika(new BaseUser(username,passwordText.hashCode()));
                } catch (BazaPodatakaException e) {
                    createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                    Logging.logger.error(e.getMessage(),e);
                }catch (DatotekaException e){
                    createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
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
            createAlert("Neuspjesan unos", "Pogreska prilikom unosa korisnika", "Potrebno je popuniti sva polja!\n Niste unjeli: "+praznaPolja, Alert.AlertType.INFORMATION);
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
                if (BazaPodataka.usernameExists(username)){
                    ispravan=false;
                    System.out.println("Potrebno je unjeti unikatan novu username");
                }
            }
            if (ispravan) {
                Korisnik odabraniKorisnik = korisnikTableView.getSelectionModel().getSelectedItem();
                if (createAlertWithResponse("Promjena korisnika","Zelite li promjeniti korisnika?","Jeste li sigurni da zelite promjeniti korisnika +"+odabraniKorisnik.getUsername())) {
                    try {
                        BazaPodataka.promjeniKorisnika(odabraniKorisnik.getId(), username, email, ime, prezime, passwordHash, razinaOvlasti);
                    } catch (BazaPodatakaException e) {
                        createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                        Logging.logger.error(e.getMessage(),e);
                    }
                    if (passwordHash != null) {
                        try {
                            DatotekaKorisnika.promjeniPassword(new BaseUser(odabraniKorisnik.getUsername(), passwordHash));
                        } catch (DatotekaException e) {
                            createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
                            Logging.logger.error(e.getMessage(),e);
                        }
                    }
                    if (!username.isEmpty()) {
                        try {
                            DatotekaKorisnika.promjeniUsername(odabraniKorisnik.getUsername(), username);
                        } catch (DatotekaException e) {
                            createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
                            Logging.logger.error(e.getMessage(),e);
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
            createAlert("Neuspjesna promjena", "Korisnik nije oznacen", "Potrebno je oznaciti korisnika u tablici koji se zeli promjeniti", Alert.AlertType.INFORMATION);
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
        if (korisnikTableView.getSelectionModel().getSelectedItem()!=null){
            Korisnik odabraniKorisnik = korisnikTableView.getSelectionModel().getSelectedItem();
            if (odabraniKorisnik.getId().equals(GlavnaAplikacija.getKorisnik().getId())){
                createAlert("Neuspjesno brisanje", "Nije moguce obrisati korisnika", "Nemoguce je obrisati trenutno prijavljenog korisnika!", Alert.AlertType.INFORMATION);
            }else{
                if (createAlertWithResponse("Brisanje korisnika", "Zelite li obrisati korisnika?", "Jeste li sigurni da zelite obrisati korisnika "+odabraniKorisnik.getUsername())) {
                    try {
                        BazaPodataka.obrisiKorisnika(odabraniKorisnik.getId());
                        DatotekaKorisnika.obrisiKorisnika(odabraniKorisnik.getUsername());
                    } catch (BazaPodatakaException e) {
                        createAlert("Greska", "Dogodila se greska pri radu s bazom", e.getMessage(), Alert.AlertType.ERROR);
                        Logging.logger.error(e.getMessage(),e);
                    } catch (DatotekaException e) {
                        createAlert("Greska", "Dogodila se greska pri radu sa datotekom", e.getMessage(), Alert.AlertType.ERROR);
                        Logging.logger.error(e.getMessage(),e);
                    }
                }
            }
        }else{
            createAlert("Neuspjesno brisanje", "Nije moguce obrisati korisnika", "Potrebno je odabrati korisnika kojeg zelite obrisati!", Alert.AlertType.INFORMATION);
        }
        initialize();
        korisnikTableView.getSelectionModel().clearSelection();
    }
}
