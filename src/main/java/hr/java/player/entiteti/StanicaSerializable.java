package hr.java.player.entiteti;
public class StanicaSerializable extends Entitet {
    String name,country,url,tags,codec;
    Integer bitrate;

    public StanicaSerializable(Long id, String name, String country, String url, String tags, String codec, Integer bitrate) {
        super(id);
        this.name = name;
        this.country = country;
        this.url = url;
        this.tags = tags;
        this.codec = codec;
        this.bitrate = bitrate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }
}
