package hr.java.player.entiteti;

public enum RazinaOvlasti {
    USER(0),
    ADMIN(1);
    final int razina;

    RazinaOvlasti(int razina) {
        this.razina = razina;
    }

    public int getRazina() {
        return razina;
    }
}
