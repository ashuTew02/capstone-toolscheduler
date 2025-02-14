package com.capstone.toolscheduler.dto.event;

import com.capstone.toolscheduler.model.EventType;

public interface Event<T> {
    EventType getType();
    T getPayload();
    String getEventId();
}
