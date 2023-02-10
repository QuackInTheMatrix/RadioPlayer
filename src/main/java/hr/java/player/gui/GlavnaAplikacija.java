package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.Korisnik;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class GlavnaAplikacija extends Application {
    private static Stage mainStage;
    private static MediaPlayer mediaPlayer;
    private static Double currentVolume=0.1;
    private static Korisnik prijavljeniKorisnik;
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

    public static void prijaviKorisnika(String username){
        prijavljeniKorisnik = BazaPodataka.dohvatiKorisnike(null,username,"","","",null,null).get(0);
        //TODO: alert sa obavjesti da je prijava izvrsena uspjesno
    }

    public static void odjaviKorisnika(){
        prijavljeniKorisnik=null;
    }

    public static MediaPlayer.Status getStatus(){
        if (mediaPlayer!=null){
            return mediaPlayer.getStatus();
        }
        return MediaPlayer.Status.UNKNOWN;
    }

    public static void changeVolume(Double newVolume){
        currentVolume=newVolume/100.0;
        if (mediaPlayer!=null){
            mediaPlayer.setVolume(currentVolume);
        }
    }

    public static void playMedia(String url){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Media media = new Media(url);
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