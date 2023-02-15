package hr.java.player.threadovi;

import hr.java.player.gui.GlavnaAplikacija;
import javafx.scene.media.MediaPlayer;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.IOException;

public class DohvatiTitle implements Runnable{
    @Override
    public void run() {
        final String lokacijaFFprobe = "/usr/bin/ffprobe";
        FFprobe fFprobe;
        try {
            fFprobe = new FFprobe(lokacijaFFprobe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FFmpegProbeResult fFmpegProbeResult;
        FFmpegFormat fFmpegFormat;
        String streamTitle,streamName;
        while (true) {
            if (GlavnaAplikacija.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                try {
                    fFmpegProbeResult = fFprobe.probe(GlavnaAplikacija.getMedia().getSource());
                    fFmpegFormat = fFmpegProbeResult.getFormat();
                    streamTitle = fFmpegFormat.tags.get("StreamTitle");
                    streamName = fFmpegFormat.tags.get("icy-name");
                    if (streamTitle != null && !streamTitle.isEmpty()) {
                        GlavnaAplikacija.setDohvaceniTitle(streamTitle);
                    } else if (streamName != null && !streamName.isEmpty()) {
                        GlavnaAplikacija.setDohvaceniTitle(streamName);
                    }else{
                        GlavnaAplikacija.setDohvaceniTitle("Nemoguce dohvatiti pjesmu");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
