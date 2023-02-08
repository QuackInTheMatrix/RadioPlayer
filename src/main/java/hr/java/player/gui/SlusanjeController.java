package hr.java.player.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class SlusanjeController {
    @FXML
    private Label trenutnaPjesmaLabel, trenutniRadioLabel, trenutnaZemljaLabel, trenutniJezikLabel, trenutniBitrateLabel;
    @FXML
    private Slider volumeSlider;

    @FXML
    void initialize() {
        volumeSlider.valueProperty().addListener(((observableValue, oldValue, newValue) -> GlavnaAplikacija.changeVolume((Double) newValue)));
        //TODO: inicijalizacija tablice
        if (GlavnaAplikacija.isPlaying()){
            //TODO: updateaj sve podatke o radiju
        }
    }
    public void pokreniRadio(){
        //TODO: dodati uzimanje oznacenog radija iz tableview-a


//        Station trazena = browser.listStationsBy(SearchMode.BYNAME, "Otvoreni")
//                .limit(100)
//                .collect(Collectors.toList()).get(0);
//
//        browser.listStationsBy(SearchMode.BYCOUNTRY, "Croatia").forEach(station -> System.out.println(station.getName() + " " + station.getUrl()+" "+station.getBitrate()));

        GlavnaAplikacija.playMedia("http://21223.live.streamtheworld.com/PROGRAM2.mp3");
        //TODO: updateati podatke o radiju koji svira
    }

    public void pauzirajRadio(){
        GlavnaAplikacija.stopMedia();
    }
}
