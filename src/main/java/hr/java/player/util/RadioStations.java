package hr.java.player.util;

import de.sfuhrm.radiobrowser4j.RadioBrowser;
import de.sfuhrm.radiobrowser4j.SearchMode;
import de.sfuhrm.radiobrowser4j.Station;

import java.util.List;
import java.util.stream.Collectors;

public class RadioStations {
    private static final RadioBrowser browser = new RadioBrowser(5000, "Demo agent/1.0");
    public static List<Station> dohvatiStanice(){
        List<Station> dohvaceneStanice = browser.listStations().limit(150).collect(Collectors.toList());
        return dohvaceneStanice;
    }
    public static List<Station> dohvatiStanice(SearchMode nacin, String vrjednost){
        List<Station> stanice = browser.listStationsBy(nacin,vrjednost)
                .limit(150)
                .collect(Collectors.toList());
        return stanice;
    }
}
