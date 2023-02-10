package hr.java.player.entiteti;

import de.sfuhrm.radiobrowser4j.Station;

public class Stanica {
    Long id;
    Station stanica;

    public Stanica(Long id, Station stanica) {
        this.id = id;
        this.stanica = stanica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Station getStanica() {
        return stanica;
    }

    public void setStanica(Station stanica) {
        this.stanica = stanica;
    }
}
