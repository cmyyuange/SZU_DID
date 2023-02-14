package org.example;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class WindowListen implements EventHandler<WindowEvent> {

    @Override
    public void handle(WindowEvent event) {
        event.consume();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("退出");
        alert.setHeaderText(null);
        alert.setContentText("是否退出程序？");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            Platform.exit();
        }
    }
}
