package com.capstone.toolscheduler.dto.event.payload;

import com.capstone.toolscheduler.model.JobStatus;

public class AckScanParseJobEventPayload {
    private String jobId;
    private JobStatus jobStatus;

    public AckScanParseJobEventPayload() {}

    public AckScanParseJobEventPayload(String jobId, JobStatus jobStatus) {
        this.jobId = jobId;
        this.jobStatus = jobStatus;
    }
    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public JobStatus getJobStatus() {
        return jobStatus;
    }
    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

}
