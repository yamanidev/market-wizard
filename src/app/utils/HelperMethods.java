package app.utils;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelperMethods {


    public static Stage openWindow(String fxmlFile, String windowTitle) throws IOException {
        Parent root = FXMLLoader.load(HelperMethods.class.getResource("../../view/" + fxmlFile));
        Scene scene = new Scene(root);
        Stage window = new Stage();
        Platform.runLater(root::requestFocus);
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Platform.runLater(root::requestFocus);
            }
        });
        window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                window.close();
            }
        });
        window.setScene(scene);
        window.setTitle(windowTitle);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.show();
        return window;
    }

    public static void emptyFieldsAlert(Stage window){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(String.valueOf("infoMessage"));
        alert.setTitle("title");
        alert.setHeaderText("headerText");
        alert.initOwner(window);
        alert.showAndWait();
    }

    public static boolean isNumeric(String s){
        try{
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

}
