package org.bashaer.bashaerfoundationcharity.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainDashboardController {
    @FXML
    private TabPane portalTabPane;

    @FXML
    public void initialize() {
        System.out.println("Main Dashboard initialized successfully with multi-role tabs.");
    }

    @FXML
    protected void handleOpenEventManager() {
        openNewWindow("/org/bashaer/bashaerfoundationcharity/views/event_manager.fxml", "Events Manager Portal");
    }

    @FXML
    protected void handleOpenVolunteerPortal() {
        openNewWindow("/org/bashaer/bashaerfoundationcharity/views/volunteer_ui.fxml", "Volunteer Observer Window");
    }

    private void openNewWindow(String absoluteFxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteFxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to open window [" + absoluteFxmlPath + "]: " + e.getMessage());
            e.printStackTrace();
        }
    }
}