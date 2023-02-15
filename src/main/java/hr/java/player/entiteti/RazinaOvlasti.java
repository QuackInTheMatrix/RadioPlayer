package hr.java.player.entiteti;

import java.io.Serializable;

public enum RazinaOvlasti implements Serializable {
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
