package com.capstone.toolscheduler.model;

public enum EventType {
    SCAN_REQUEST,
    SCAN_PARSE,
    STATE_UPDATE,

    SCAN_REQUEST_JOB,
    SCAN_PARSE_JOB,
    STATE_UPDATE_JOB,

    ACK_SCAN_REQUEST_JOB,
    ACK_SCAN_PARSE_JOB,
    ACK_STATE_UPDATE_JOB,

    TICKET_CREATE,
    TICKET_UPDATE_STATUS,

    TICKET_CREATE_JOB,
    TICKET_UPDATE_STATUS_JOB,

    ACK_TICKET_CREATE_JOB,
    ACK_TICKET_UPDATE_STATUS_JOB,


}
