package org.bashaer.bashaerfoundationcharity.pattern.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class VolunteerObserver implements Observer {
    private final String volunteerName;
    private Consumer<String> uiCallback;

    public VolunteerObserver(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public void setUiCallback(Consumer<String> uiCallback) {
        this.uiCallback = uiCallback;
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = "[Alert for Volunteer " + volunteerName + "]: " + arg;
        System.out.println(message);

        if (uiCallback != null) {
            javafx.application.Platform.runLater(() -> uiCallback.accept(message));
        }
    }
}