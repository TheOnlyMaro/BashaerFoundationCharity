package org.bashaer.bashaerfoundationcharity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.bashaer.bashaerfoundationcharity.pattern.observer.EventSubject;
import org.bashaer.bashaerfoundationcharity.pattern.observer.VolunteerObserver;

import java.util.Observable;
import java.util.Observer;

public class EventDashboardController {
    @FXML private TextField eventNameField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea logArea;

    private EventSubject activeEvent;
    private VolunteerObserver sampleVolunteer;

    @FXML
    public void initialize() {
        statusCombo.getItems().addAll("Upcoming", "Active & Accepting Donations", "Completed", "Cancelled");
    }

    @FXML
    protected void handleStatusUpdate() {
        String eventName = eventNameField.getText();
        String newStatus = statusCombo.getValue();

        if (eventName == null || eventName.trim().isEmpty() || newStatus == null) {
            logArea.appendText("Please enter an event name and select a status.\n");
            return;
        }

        // Initialize event subject if not already created
        if (activeEvent == null || !activeEvent.getEventName().equals(eventName)) {
            activeEvent = new EventSubject(eventName);

            // Register a custom observer that logs directly to our UI text area
            sampleVolunteer = new VolunteerObserver("Ahmad (Lead Volunteer)") {
                @Override
                public void update(Observable o, Object arg) {
                    logArea.appendText("[NOTIFICATION]: " + arg + "\n");
                }
            };
            activeEvent.addObserver((Observer) sampleVolunteer);
            logArea.appendText("Created event and registered volunteer observer.\n");
        }

        // Trigger state change (automatically fires Observer notifications)
        activeEvent.setStatus(newStatus);
    }
}