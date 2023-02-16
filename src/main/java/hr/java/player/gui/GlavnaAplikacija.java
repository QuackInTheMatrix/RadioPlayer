package hr.java.player.gui;

import hr.java.player.baza.BazaPodataka;
import hr.java.player.entiteti.Korisnik;
import hr.java.player.entiteti.Promjena;
import hr.java.player.entiteti.Promjene;
import hr.java.player.threadovi.DohvatiTitle;
import hr.java.player.threadovi.UpdateTitle;
import hr.java.player.util.Serijazilacija;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlavnaAplikacija extends Application {
    private static Stage mainStage;
    private static Media media;
    private static MediaPlayer mediaPlayer;
    private static Double currentVolume=0.1;
    private static Korisnik prijavljeniKorisnik;
    private static Duration lastKnowDuration;
    private static Promjene svePromjene;
    private static String dohvaceniTitle;
    private static boolean dohvaceniIsUsed=false;
    public static boolean shutdown=false;
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(GlavnaAplikacija.class.getResource("pocetna.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        setTitle("Radio Player");
        stage.setScene(scene);
        stage.show();
        if (Serijazilacija.fileExists()){
            svePromjene=Serijazilacija.deserijaliziraj();
        }else{
            svePromjene = new Promjene(new ArrayList<>());
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new DohvatiTitle());
        executorService.shutdown();
        mainStage.setOnCloseRequest(t -> {
            shutdown=true;
            System.exit(0);
        });
        Timeline promjeniTitle = new Timeline(new KeyFrame(Duration.seconds(1), event -> Platform.runLater(new UpdateTitle(getTitle(),dohvaceniTitle))));
        promjeniTitle.setCycleCount(Timeline.INDEFINITE);
        promjeniTitle.play();
    }
    public static void dodajPromjenu(Promjena promjena){
        svePromjene.addPromjena(promjena);
        Serijazilacija.serijaliziraj(svePromjene);
    }

    public static Promjene getSvePromjene(){
        return svePromjene;
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

    public static MediaPlayer.Status getStatus(){
        if (mediaPlayer!=null){
            return mediaPlayer.getStatus();
        }
        return MediaPlayer.Status.UNKNOWN;
    }
    public static MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public static Double getCurrentVolume(){
        return currentVolume;
    }

    public static void changeVolume(Double newVolume){
        currentVolume=newVolume/100.0;
        if (mediaPlayer!=null){
            mediaPlayer.setVolume(currentVolume);
        }
    }

    public static boolean isDohvaceniIsUsed() {
        return dohvaceniIsUsed;
    }

    public static void setDohvaceniIsUsed(boolean dohvaceniIsUsed) {
        GlavnaAplikacija.dohvaceniIsUsed = dohvaceniIsUsed;
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

    public synchronized static void setDohvaceniTitle(String dohvaceniTitle) {
        GlavnaAplikacija.dohvaceniTitle = dohvaceniTitle;
    }

    public static void setTitle(String newTitle){
        dohvaceniTitle =newTitle;
        mainStage.setTitle(dohvaceniTitle);
    }

    public static String getDohvaceniTitle() {
        return dohvaceniTitle;
    }

    public static String getTitle() {
        return mainStage.getTitle();
    }

    public static void main(String[] args) {
        launch();
    }
}