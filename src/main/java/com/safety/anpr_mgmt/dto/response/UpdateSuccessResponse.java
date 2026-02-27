package com.safety.anpr_mgmt.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class UpdateSuccessResponse {
    private Long id;
    private Long no_violations_observed;
    private Long no_employees_found;
    private Long no_contractor_found;
    private Long record_not_found;
    private Instant createdAt;
}
