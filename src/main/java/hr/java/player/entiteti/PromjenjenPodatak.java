package hr.java.player.entiteti;

import java.io.Serializable;

public class PromjenjenPodatak<T> implements Serializable {
    T staraVrjednost;
    T novaVrjednost;

    public PromjenjenPodatak(T staraVrjednost, T novaVrjednost) {
        this.staraVrjednost = staraVrjednost;
        this.novaVrjednost = novaVrjednost;
    }

    public T getStaraVrjednost() {
        return staraVrjednost;
    }

    public void setStaraVrjednost(T staraVrjednost) {
        this.staraVrjednost = staraVrjednost;
    }

    public T getNovaVrjednost() {
        return novaVrjednost;
    }

    public void setNovaVrjednost(T novaVrjednost) {
        this.novaVrjednost = novaVrjednost;
    }
}
