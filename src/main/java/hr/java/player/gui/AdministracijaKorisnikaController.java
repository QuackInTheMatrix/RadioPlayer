package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.Korisnik;
import hr.java.player.entiteti.RazinaOvlasti;
import hr.java.player.util.DatotekaKorisnika;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdministracijaKorisnikaController {
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
                //TODO: alert umjesto sout
                System.out.println("Potrebno je unjeti unikatno ime!");
            }else{
                BazaPodataka.unesiKorisnika(username,email,ime,prezime,passwordText.hashCode(),razinaOvlasti);
                DatotekaKorisnika.unesiKorisnika(username,passwordText.hashCode());
            }
            initialize();
        }else{
            //TODO: alert umjesto sout
            System.out.println("Potrebno je popuniti sva polja za unos novog korisnika!");
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
            if (ispravan){
                Korisnik odabraniKorisnik = korisnikTableView.getSelectionModel().getSelectedItem();
                BazaPodataka.promjeniKorisnika(odabraniKorisnik.getId(),username,email,ime,prezime,passwordHash,razinaOvlasti);
                if (passwordHash!=null){
                    DatotekaKorisnika.promjeniPassword(odabraniKorisnik.getUsername(),passwordHash);
                }
                if (!username.isEmpty()){
                    DatotekaKorisnika.promjeniUsername(odabraniKorisnik.getUsername(),username);
                }
                if (GlavnaAplikacija.getKorisnik().getId().equals(odabraniKorisnik.getId())){
                    if (!username.isEmpty()){
                        GlavnaAplikacija.prijaviKorisnika(username);
                    }else{
                        GlavnaAplikacija.prijaviKorisnika(odabraniKorisnik.getUsername());
                    }
                }
                korisnikTableView.getSelectionModel().clearSelection();
                initialize();
            }
        }else{
            //TODO: alert umjesto sout
            System.out.println("Potrebno je odabrati korisnika u tablici");
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
                //TODO: alert umjesto sout
                System.out.println("Nemoguce je obrisati trenutno prijavljenog korisnika!");
            }else{
                //TODO: alert umjesto sout
                System.out.println("Jeste li sigurni da zelite obrisati korisnika +"+odabraniKorisnik.getUsername());
                BazaPodataka.obrisiKorisnika(odabraniKorisnik.getId(),"","","","",null,null);
                DatotekaKorisnika.obrisiKorisnika(odabraniKorisnik.getUsername());
            }
        }else{
            System.out.println("Potrebno je odabrati korisnika kojeg zelite obrisati!");
        }
        initialize();
        korisnikTableView.getSelectionModel().clearSelection();
    }
}
