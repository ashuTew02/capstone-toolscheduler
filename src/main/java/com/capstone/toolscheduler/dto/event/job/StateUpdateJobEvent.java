package com.capstone.toolscheduler.dto.event.job;

import java.util.UUID;

import com.capstone.toolscheduler.dto.event.Event;
import com.capstone.toolscheduler.dto.event.payload.job.StateUpdateJobEventPayload;
import com.capstone.toolscheduler.model.EventType;

public final class StateUpdateJobEvent implements Event<StateUpdateJobEventPayload> {
    private StateUpdateJobEventPayload payload;
    private String eventId;
    private EventType type = EventType.STATE_UPDATE_JOB;


    public StateUpdateJobEvent(StateUpdateJobEventPayload payload) {
        this.eventId = UUID.randomUUID().toString();
        this.payload = payload;
    }

    
    public StateUpdateJobEvent() {
        this.eventId = UUID.randomUUID().toString();
    }


    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public StateUpdateJobEventPayload getPayload() {
        return payload;
    }

    @Override
    public String getEventId() {
        return eventId;
    }
}
