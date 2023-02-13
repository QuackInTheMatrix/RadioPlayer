package hr.java.player.entiteti;

public class Korisnik extends Osoba{
    BaseUser baseUser;
    String email;
    RazinaOvlasti razinaOvlasti;

    public Korisnik(String username, Integer passwordHash, String ime, String prezime, String email, RazinaOvlasti razinaOvlasti, Long id) {
        super(id, ime, prezime);
        this.baseUser = new BaseUser(username,passwordHash);
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.razinaOvlasti = razinaOvlasti;
    }

    public String getUsername() {
        return baseUser.username();
    }


    public Integer getPasswordHash() {
        return baseUser.passwordHash();
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public RazinaOvlasti getRazinaOvlasti() {
        return razinaOvlasti;
    }

    public void setRazinaOvlasti(RazinaOvlasti razinaOvlasti) {
        this.razinaOvlasti = razinaOvlasti;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
