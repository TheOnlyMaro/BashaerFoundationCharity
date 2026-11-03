package org.bashaer.bashaerfoundationcharity.pattern.observer;

import java.util.Observable;

public class EventSubject extends Observable {
    private final String categoryName;

    public EventSubject(String categoryName) {
        this.categoryName = categoryName;
    }

    public void triggerUpdate(String message) {
        setChanged();
        notifyObservers(message);
    }

    public String getCategoryName() {
        return categoryName;
    }
}