package com.dyakushev.event;


import java.util.*;

public class EventManager {
    Map<EventOperation, List<EventListener>> listeners = new HashMap<>();

    public EventManager(EventOperation... eventOperations) {
        for (EventOperation eventOperation : eventOperations) {
            this.listeners.put(eventOperation, new ArrayList<>());
        }
    }

    public void subscribe(EventOperation eventType, EventListener listener) {

        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(EventOperation eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(EventOperation eventType, String eventText) {
        List<EventListener> users = listeners.get(eventType);

        if (users!=null && !users.isEmpty())
            for (EventListener listener : users) {
                listener.update(eventType, eventText);
            }
    }

}

