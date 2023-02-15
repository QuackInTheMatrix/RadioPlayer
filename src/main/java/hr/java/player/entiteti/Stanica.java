package hr.java.player.entiteti;

import de.sfuhrm.radiobrowser4j.Station;

import java.io.Serializable;

public class Stanica extends Entitet implements Serializable {
    private Station stanica;

    public static class Builder{
        private String naziv,zemlja, codec, tags, url;
        private Integer bitrate;
        private Long id;

        public Builder(Long id, String url) {
            this.id = id;
            this.url=url;
        }
        public Builder withNaziv(String naziv){
            this.naziv=naziv;
            return this;
        }
        public Builder withZemlja(String zemlja){
            this.zemlja=zemlja;
            return this;
        }
        public Builder withCodec(String codec){
            this.codec=codec;
            return this;
        }
        public Builder withTags(String tags){
            this.tags=tags;
            return this;
        }
        public Builder withBitrate(Integer bitrate){
            this.bitrate=bitrate;
            return this;
        }
        public Stanica build(){
            Station station = new Station();
            station.setUrl(this.url);
            station.setName(this.naziv);
            station.setCountry(this.zemlja);
            station.setCodec(this.codec);
            station.setTags(this.tags);
            station.setBitrate(this.bitrate);
            return new Stanica(this.id,station);
        }
    }

    private Stanica(Long id, Station stanica) {
        super(id);
        this.stanica=stanica;
    }

    public Station getStanica() {
        return stanica;
    }

    public void setStanica(Station stanica) {
        this.stanica = stanica;
    }
}
