package app.utils;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelperMethods {

    private Parent root;
    private Stage window;
    private Scene scene;

    public void openWindow(String fxmlFile, String windowTitle) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        scene = new Scene(root);
        window = new Stage();

        Platform.runLater(() -> root.requestFocus());

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Platform.runLater(() -> root.requestFocus());
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
        window.showAndWait();

    }

}
