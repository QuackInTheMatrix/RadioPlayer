package hr.java.player.gui;

import de.sfuhrm.radiobrowser4j.RadioBrowser;
import de.sfuhrm.radiobrowser4j.SearchMode;
import de.sfuhrm.radiobrowser4j.Station;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.MediaView;
import java.util.stream.Collectors;

public class SlusanjeController {
    @FXML
    private Label trenutnaPjesmaLabel, trenutniRadioLabel, trenutnaZemljaLabel, trenutniJezikLabel, trenutniBitrateLabel;
    @FXML
    private MediaView mediaView;

    @FXML
    void initialize() {
        //TODO: inicijalizacija tablice
    }
    public void pokreniRadio(){
        //TODO: dodati uzimanje oznacenog radia iz tableview-a
//        RadioBrowser browser = new RadioBrowser(5000, "Demo agent/1.0");
//        Station trazena = browser.listStationsBy(SearchMode.BYNAME, "Otvoreni")
//                .limit(100)
//                .collect(Collectors.toList()).get(0);
//
//        browser.listStationsBy(SearchMode.BYCOUNTRY, "Croatia").forEach(station -> System.out.println(station.getName() + " " + station.getUrl()));

        GlavnaAplikacija.playMedia("http://21223.live.streamtheworld.com/PROGRAM2.mp3", mediaView);
    }

    public void pauzirajRadio(){
        GlavnaAplikacija.stopMedia();
    }
}
