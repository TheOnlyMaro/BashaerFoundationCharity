package org.bashaer.bashaerfoundationcharity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Loads the FXML file from src/main/resources/org/bashaer/bashaerfoundationcharity/views/user_dashboard.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/user_dashboard.fxml"));

        // Initialize Scene with a balanced layout size (850x650)
        Scene scene = new Scene(fxmlLoader.load(), 850, 650);

        // Configure Stage (Window) settings
        stage.setTitle("Bashaer Foundation — User Control Panel & Phase 1 Dashboard");
        stage.setScene(scene);

        // Set minimum boundaries so the control panel layout doesn't break when resized
        stage.setMinWidth(750);
        stage.setMinHeight(550);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}