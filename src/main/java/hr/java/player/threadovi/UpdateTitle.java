package hr.java.player.threadovi;

import hr.java.player.gui.GlavnaAplikacija;
import javafx.scene.media.MediaPlayer;

public class UpdateTitle implements Runnable{
    private final String stariTitle, dohvaceniTitle;

    public UpdateTitle(String stariTitle, String dohvaceniTitle) {
        this.stariTitle = stariTitle;
        this.dohvaceniTitle = dohvaceniTitle;
    }

    @Override
    public void run() {
        if (GlavnaAplikacija.getStatus().equals(MediaPlayer.Status.PLAYING) && !stariTitle.equals(dohvaceniTitle)) {
            GlavnaAplikacija.setTitle(dohvaceniTitle);
        }
    }
}
