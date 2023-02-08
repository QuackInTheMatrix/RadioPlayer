package hr.java.player.util;

import de.sfuhrm.radiobrowser4j.RadioBrowser;
import de.sfuhrm.radiobrowser4j.SearchMode;
import de.sfuhrm.radiobrowser4j.Station;

import java.util.List;
import java.util.stream.Collectors;

public class RadioStations {
    private static final RadioBrowser browser = new RadioBrowser(5000, "Demo agent/1.0");
    public static List<Station> dohvatiStanice(){
        return browser.listStations().limit(150).collect(Collectors.toList());
    }
    public static List<Station> dohvatiStanice(SearchMode nacin, String vrjednost, String naziv, String zanr, String zemlja, String codec, Integer bitrate){
        List<Station> stanice = browser.listStationsBy(nacin,vrjednost)
                .limit(150)
                .collect(Collectors.toList());
        stanice = stanice.stream()
                .filter(station -> naziv.isEmpty() || station.getName().toLowerCase().contains(naziv.toLowerCase()))
                .filter(station -> zanr.isEmpty() || station.getTags().toLowerCase().contains(zanr.toLowerCase()))
                .filter(station -> zemlja.isEmpty() || station.getCountry().toLowerCase().contains(zemlja.toLowerCase()))
                .filter(station -> codec.isEmpty() || station.getCodec().toLowerCase().contains(codec.toLowerCase()))
                .filter(station -> bitrate==null || station.getBitrate().equals(bitrate))
                .collect(Collectors.toList());
        return stanice;
    }
}
