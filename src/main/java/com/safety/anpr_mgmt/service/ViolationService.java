package com.safety.anpr_mgmt.service;

import com.safety.anpr_mgmt.dto.response.ContractorViolationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ViolationService {
    List<ContractorViolationResponse> getContractorViolations(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
