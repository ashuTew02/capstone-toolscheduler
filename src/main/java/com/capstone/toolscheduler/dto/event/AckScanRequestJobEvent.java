package com.capstone.toolscheduler.dto.event;

import java.util.UUID;

import com.capstone.toolscheduler.dto.event.payload.AckJobEventPayload;
import com.capstone.toolscheduler.model.EventType;

public class AckScanRequestJobEvent implements Event<AckJobEventPayload>{
    private AckJobEventPayload payload;
    private String eventId;

    public AckScanRequestJobEvent(AckJobEventPayload payload) {
        this.eventId = UUID.randomUUID().toString();
        this.payload = payload;
    }

    public AckScanRequestJobEvent() {
        this.eventId = UUID.randomUUID().toString();
    }

    @Override
    public EventType getType() {
        return EventType.ACK_SCAN_REQUEST_JOB;
    }

    @Override
    public AckJobEventPayload getPayload() {
        return payload;
    }

    @Override
    public String getEventId() {
        return eventId;
    }
}
