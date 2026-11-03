package org.bashaer.bashaerfoundationcharity.pattern.observer;

import java.util.Observer;
import java.util.Observable;

public class VolunteerObserver implements Observer {
    private String volunteerName;

    public VolunteerObserver(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    @Override
    public void update(Observable o, Object arg) {
        // arg receives the message passed from notifyObservers()
        System.out.println("[Alert for Volunteer " + volunteerName + "]: " + arg);
    }
}