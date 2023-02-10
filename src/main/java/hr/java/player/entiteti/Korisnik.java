package hr.java.player.entiteti;

public class Korisnik {
    String username;
    Integer passwordHash;
    String ime;
    String prezime;
    String email;
    RazinaOvlasti razinaOvlasti;
    Long id;

    public Korisnik(String username, Integer passwordHash, String ime, String prezime, String email, RazinaOvlasti razinaOvlasti, Long id) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.razinaOvlasti = razinaOvlasti;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(Integer password) {
        this.passwordHash = password;
    }

    public RazinaOvlasti getRazinaOvlasti() {
        return razinaOvlasti;
    }

    public void setRazinaOvlasti(RazinaOvlasti razinaOvlasti) {
        this.razinaOvlasti = razinaOvlasti;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
