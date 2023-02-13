package hr.java.player.entiteti;

public abstract class Osoba extends Entitet{
    String ime;
    String prezime;

    public Osoba(Long id,String ime, String prezime) {
        super(id);
        this.ime = ime;
        this.prezime = prezime;
    }
    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
}
