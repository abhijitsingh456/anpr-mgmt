package com.safety.anpr_mgmt.dto.response;

import com.safety.anpr_mgmt.enums.UploadJobStatus;
import lombok.Data;

@Data
public class UploadJobResponse {
    private Long jobId;
    private UploadJobStatus status;
    private String message;
}
