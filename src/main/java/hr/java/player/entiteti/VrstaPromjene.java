package hr.java.player.entiteti;

import java.io.Serializable;

public enum VrstaPromjene implements Serializable {
    INSERT("Insert"),
    UPDATE("Update"),
    DELETE("Delete");
    String naziv;
    VrstaPromjene(String naziv){
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
