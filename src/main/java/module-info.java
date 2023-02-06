module hr.java.player.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires tornadofx;
    requires tornadofx.controls;


    opens hr.java.player.gui to javafx.fxml;
    exports hr.java.player.gui;
}