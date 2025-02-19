package com.capstone.toolscheduler.dto.event;

import java.util.UUID;

import com.capstone.toolscheduler.dto.event.payload.StateUpdateEventPayload;
import com.capstone.toolscheduler.model.EventType;

public final class StateUpdateEvent implements Event<StateUpdateEventPayload> {
    private StateUpdateEventPayload payload;
    private String eventId;
    private EventType type = EventType.STATE_UPDATE;


    public StateUpdateEvent(StateUpdateEventPayload payload) {
        this.eventId = UUID.randomUUID().toString();
        this.payload = payload;
    }

    
    public StateUpdateEvent() {
        this.eventId = UUID.randomUUID().toString();
    }


    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public StateUpdateEventPayload getPayload() {
        return payload;
    }

    @Override
    public String getEventId() {
        return eventId;
    }
}
