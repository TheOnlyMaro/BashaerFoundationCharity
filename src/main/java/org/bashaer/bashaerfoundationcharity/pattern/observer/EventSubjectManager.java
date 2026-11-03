package org.bashaer.bashaerfoundationcharity.pattern.observer;

import java.util.HashMap;
import java.util.Map;

public class EventSubjectManager {
    private static final Map<String, EventSubject> subjects = new HashMap<>();

    public static synchronized EventSubject getSubject(String category) {
        subjects.putIfAbsent(category, new EventSubject(category));
        return subjects.get(category);
    }

    public static void broadcast(String category, String message) {
        // 1. Notify subscribers of the specific category
        getSubject(category).triggerUpdate(message);

        // 2. Also notify "Any/All" subscribers if the event wasn't already posted directly to Any/All
        if (!"Any/All".equalsIgnoreCase(category) && subjects.containsKey("Any/All")) {
            getSubject("Any/All").triggerUpdate(message);
        }
    }
}