package org.bashaer.bashaerfoundationcharity.pattern.observer;

import java.util.Observable;

public class EventSubject extends Observable {
    private String eventName;
    private String status;

    public EventSubject(String eventName) {
        this.eventName = eventName;
        this.status = "Upcoming";
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;

        // CRITICAL: java.util.Observable requires setChanged() to be called first!
        setChanged();

        // Notify all registered observers and pass the message
        notifyObservers("Event '" + eventName + "' status changed to: " + status);
    }

    public String getEventName() {
        return eventName;
    }

    public String getStatus() {
        return status;
    }
}