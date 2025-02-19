package com.capstone.toolscheduler.dto.event;

import java.util.UUID;

import com.capstone.toolscheduler.dto.event.payload.ScanParseEventPayload;
import com.capstone.toolscheduler.model.EventType;

public class ScanParseEvent implements Event<ScanParseEventPayload>{
    private ScanParseEventPayload payload;
    private String eventId;
    private EventType type = EventType.SCAN_PARSE;

    public ScanParseEvent(ScanParseEventPayload payload) {
        this.eventId = UUID.randomUUID().toString();
        this.payload = payload;
    }

    public ScanParseEvent() {
        this.eventId = UUID.randomUUID().toString();
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public ScanParseEventPayload getPayload() {
        return payload;
    }

    @Override
    public String getEventId() {
        return eventId;
    }
}
