package hr.java.player.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;

public class GlavnaAplikacija extends Application {
    private static Stage mainStage;
    private static Media media;
    private static MediaPlayer mediaPlayer;
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(GlavnaAplikacija.class.getResource("pocetna.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Radio player");
        stage.setScene(scene);
        stage.show();
    }

    public static void playMedia(String url, MediaView mediaView){
        media = new Media(url);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }

    public static void PlayMedia(){
        if (media!=null && mediaPlayer!=null){
            mediaPlayer.play();
        }
    }

    public static void stopMedia(){
        if (media!=null && mediaPlayer!=null) {
            mediaPlayer.stop();
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