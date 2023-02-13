package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.Station;

import java.util.List;
import java.util.stream.Collectors;

public interface Pretrazljiv {
    public default List<Station> filtrirajStanice(List<Station> stanice,String naziv, String zanr, String zemlja, String codec, Integer bitrate){
        return stanice.stream()
                .filter(station -> naziv.isEmpty() || station.getName().toLowerCase().contains(naziv.toLowerCase()))
                .filter(station -> zanr.isEmpty() || station.getTags().toLowerCase().contains(zanr.toLowerCase()))
                .filter(station -> zemlja.isEmpty() || station.getCountry().toLowerCase().contains(zemlja.toLowerCase()))
                .filter(station -> codec.isEmpty() || station.getCodec().toLowerCase().contains(codec.toLowerCase()))
                .filter(station -> bitrate==null || station.getBitrate().equals(bitrate))
                .collect(Collectors.toList());
    }
}
