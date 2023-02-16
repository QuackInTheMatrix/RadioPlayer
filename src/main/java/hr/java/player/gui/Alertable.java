package hr.java.player.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public interface Alertable {
    public default void createAlert(String title, String subTitle, String description, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(subTitle);
        alert.setContentText(description);
        alert.showAndWait();
    }
    public default boolean createAlertWithResponse(String title, String subTitle, String description){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(subTitle);
        alert.setContentText(description);
        ButtonType daButton = new ButtonType("Da");
        ButtonType neButton = new ButtonType("Ne");
        alert.getButtonTypes().setAll(daButton, neButton);
        Optional<ButtonType> odabraniButton = alert.showAndWait();
        return odabraniButton.get() == daButton;
    }
}
