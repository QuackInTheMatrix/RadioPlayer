package hr.java.player.threadovi;

import hr.java.player.gui.GlavnaAplikacija;
import hr.java.player.util.Logging;
import javafx.scene.media.MediaPlayer;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.IOException;

public class DohvatiTitle implements Runnable{
    @Override
    public void run() {
        final String lokacijaFFprobe = "/usr/bin/ffprobe";
        FFprobe fFprobe = null;
        try {
            fFprobe = new FFprobe(lokacijaFFprobe);
        } catch (IOException e) {
            Logging.logger.error(e.getMessage(),e);
        }
        FFmpegProbeResult fFmpegProbeResult;
        FFmpegFormat fFmpegFormat;
        String streamTitle,streamName;
        while (!GlavnaAplikacija.shutdown) {
                if (GlavnaAplikacija.getMediaPlayer()!=null && GlavnaAplikacija.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                    try {
                        fFmpegProbeResult = fFprobe.probe(GlavnaAplikacija.getMedia().getSource());
                        fFmpegFormat = fFmpegProbeResult.getFormat();
                        streamTitle = fFmpegFormat.tags.get("StreamTitle");
                        streamName = fFmpegFormat.tags.get("icy-name");
                        if (streamTitle != null && !streamTitle.isEmpty()) {
                            Thread thread = new Thread(new PohraniTitle(streamTitle));
                            thread.start();
                        } else if (streamName != null && !streamName.isEmpty()) {
                            Thread thread = new Thread(new PohraniTitle(streamName));
                            thread.start();
                        } else {
                            Thread thread = new Thread(new PohraniTitle("Nemoguce dohvatiti pjesmu"));
                            thread.start();
                        }
                    } catch (IOException e) {
                        Logging.logger.error(e.getMessage(), e);
                    }
                } else {
                    Thread thread = new Thread(new PohraniTitle("Radio Player"));
                    thread.start();
                }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Logging.logger.error(e.getMessage(),e);
            }
        }
        }
    }
