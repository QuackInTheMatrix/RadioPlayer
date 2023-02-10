package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.Korisnik;
import hr.java.player.util.DatotekaKorisnika;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PromjeniKorisnikaController {
    @FXML
    private TextField usernameField, passwordField, imeField, prezimeField, emailField;
    private Korisnik korisnik;
    @FXML
    void initialize(){
        korisnik = GlavnaAplikacija.getKorisnik();
        usernameField.setText(korisnik.getUsername());
        imeField.setText(korisnik.getIme());
        prezimeField.setText(korisnik.getPrezime());
        emailField.setText(korisnik.getEmail());
    }

    @FXML
    void promjeni(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String email = emailField.getText();
        Integer passwordHash;
        if (password.isEmpty()){
            passwordHash= null;
        }else{
            passwordHash = passwordField.getText().hashCode();
        }
        if (!BazaPodataka.dohvatiKorisnike(null,username,"","","",null,null).isEmpty()){
            if (!username.equals(korisnik.getUsername())){
                username="";
                //TODO: zamjeniti sout sa alert
                System.out.println("Korisnicko ime je zauzeto!");
            }
        }
        BazaPodataka.promjeniKorisnika(korisnik.getId(),username,email,ime,prezime,passwordHash,null);
        if (!password.isEmpty() && !passwordHash.equals(korisnik.getPasswordHash())){
            DatotekaKorisnika.promjeniPassword(korisnik.getUsername(),passwordHash);
        }
        if (!username.isEmpty() && !username.equals(korisnik.getUsername())){
            DatotekaKorisnika.promjeniUsername(korisnik.getUsername(),username);
            GlavnaAplikacija.prijaviKorisnika(username);
        }else{
            GlavnaAplikacija.prijaviKorisnika(korisnik.getUsername());
        }
        korisnik=GlavnaAplikacija.getKorisnik();
    }
}
