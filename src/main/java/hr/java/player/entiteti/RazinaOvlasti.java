package hr.java.player.entiteti;

public enum RazinaOvlasti {
    USER(0,"User"),
    ADMIN(1,"Admin");
    final int razina;
    final String naziv;

    RazinaOvlasti(int razina, String naziv) {
        this.razina = razina;
        this.naziv = naziv;
    }

    public int getRazina() {
        return razina;
    }

    public String getNaziv() {
        return naziv;
    }
}
