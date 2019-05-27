package com.dyakushev.event;

import com.dyakushev.event.EventOperation;

public interface EventListener {
    void update(EventOperation eventOperation, String eventText);
}
