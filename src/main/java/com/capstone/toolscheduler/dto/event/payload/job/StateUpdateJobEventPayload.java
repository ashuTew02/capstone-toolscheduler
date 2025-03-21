package com.capstone.toolscheduler.dto.event.payload.job;

import com.capstone.toolscheduler.dto.event.payload.StateUpdateEventPayload;
import com.capstone.toolscheduler.model.FindingState;
import com.capstone.toolscheduler.model.Tool;

public final class StateUpdateJobEventPayload {
    private String esFindingId;
    private Long tenantId;
    private Tool tool;

    private String owner;
    private String repository;
    private Long alertNumber;
    private FindingState updatedState;
    private String service = "github";
    Long jobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    
    public StateUpdateJobEventPayload(String esFindingId, Long tenantId, Tool tool, String owner, String repository,
            Long alertNumber, String service, FindingState updatedState, Long jobId) {
        this.esFindingId = esFindingId;
        this.tenantId = tenantId;
        this.tool = tool;
        this.owner = owner;
        this.repository = repository;
        this.alertNumber = alertNumber;
        this.service = service;
        this.updatedState = updatedState;
        this.jobId = jobId;
    }
    public StateUpdateJobEventPayload(String esFindingId, Long tenantId, Tool tool, String owner, String repository,
            Long alertNumber, FindingState updatedState, Long jobId) {
        this.esFindingId = esFindingId;
        this.tenantId = tenantId;
        this.tool = tool;
        this.owner = owner;
        this.repository = repository;
        this.alertNumber = alertNumber;
        this.updatedState = updatedState;
        this.jobId = jobId;
        this.service = "github";
    }

    public StateUpdateJobEventPayload(Long jobId, StateUpdateEventPayload payload) {
        this.esFindingId = payload.getEsFindingId();
        this.tenantId = payload.getTenantId();
        this.tool = payload.getTool();
        this.owner = payload.getOwner();
        this.repository = payload.getRepository();
        this.alertNumber = payload.getAlertNumber();
        this.updatedState = payload.getUpdatedState();
        this.jobId = jobId;
        this.service = "github";
    }

    public StateUpdateJobEventPayload() {}

    public String getEsFindingId() {
        return esFindingId;
    }
    public void setEsFindingId(String esFindingId) {
        this.esFindingId = esFindingId;
    }
    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    public Tool getTool() {
        return tool;
    }
    public void setTool(Tool tool) {
        this.tool = tool;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getRepository() {
        return repository;
    }
    public void setRepository(String repository) {
        this.repository = repository;
    }
    public Long getAlertNumber() {
        return alertNumber;
    }
    public void setAlertNumber(Long alertNumber) {
        this.alertNumber = alertNumber;
    }
    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public FindingState getUpdatedState() {
        return updatedState;
    }
    public void setUpdatedState(FindingState updatedState) {
        this.updatedState = updatedState;
    }

    



}
