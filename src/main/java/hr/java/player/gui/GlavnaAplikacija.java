package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.Korisnik;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class GlavnaAplikacija extends Application {
    private static Stage mainStage;
    private static Media media;
    private static MediaPlayer mediaPlayer;
    private static Double currentVolume=0.1;
    private static Korisnik prijavljeniKorisnik;
    private static Duration lastKnowDuration;
    //private static FFmpeg = new FFm
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(GlavnaAplikacija.class.getResource("pocetna.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Radio player");
        stage.setScene(scene);
        stage.show();
    }

    public static boolean isLoggedIn(){
        return prijavljeniKorisnik!=null;
    }

    public static Korisnik getKorisnik(){
        return prijavljeniKorisnik;
    }

    public static Media getMedia(){
        return media;
    }

    public static void prijaviKorisnika(String username){
        prijavljeniKorisnik = BazaPodataka.dohvatiKorisnike(null,username,"","","",null,null).get(0);
    }

    public static void odjaviKorisnika(){
        prijavljeniKorisnik=null;
    }
    public static void fixPlayback(){
        if (mediaPlayer!=null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING) && mediaPlayer.getCurrentTime().equals(lastKnowDuration)){
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer=new MediaPlayer(media);
            mediaPlayer.play();
        }
        lastKnowDuration=mediaPlayer.getCurrentTime();
    }

    public static MediaPlayer.Status getStatus(){
        if (mediaPlayer!=null){
            return mediaPlayer.getStatus();
        }
        return MediaPlayer.Status.UNKNOWN;
    }
    public static MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public static void changeVolume(Double newVolume){
        currentVolume=newVolume/100.0;
        if (mediaPlayer!=null){
            mediaPlayer.setVolume(currentVolume);
            //TODO: maknuti nakon testiranja
            System.out.println("Current time:"+mediaPlayer.getCurrentTime());
            System.out.println("Stop time:"+mediaPlayer.getStopTime());
            //fixPlayback();
        }
    }

    public static void playMedia(String url){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer.onEndOfMediaProperty().unbind();
        }
        media = new Media(url);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(currentVolume);
        mediaPlayer.play();
    }

    public static void PlayMedia(){
        if (mediaPlayer!=null){
            mediaPlayer.play();
        }
    }

    public static void pauseMedia(){
        if (mediaPlayer!=null) {
            mediaPlayer.pause();
        }
    }

    public static void setNewStage(BorderPane root){
        Scene scene = new Scene(root,600,500);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}