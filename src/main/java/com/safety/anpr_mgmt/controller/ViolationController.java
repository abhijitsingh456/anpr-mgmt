package com.safety.anpr_mgmt.controller;


import com.safety.anpr_mgmt.dto.response.ContractorViolationResponse;
import com.safety.anpr_mgmt.dto.response.UploadJobResponse;
import com.safety.anpr_mgmt.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/violation")
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationService violationService;

    @GetMapping("/contractor/start_date/{start_date}/end_date/{end_date}")
    public ResponseEntity<List<ContractorViolationResponse>> getStatus(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date,
                                                                       @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end_date){

        LocalDateTime startDateTime = start_date.atStartOfDay();
        LocalDateTime endDateTime = end_date.atTime(23,59,59);

        List<ContractorViolationResponse> contractorViolationResponse = violationService.getContractorViolations(startDateTime, endDateTime);

        return ResponseEntity.ok().body(contractorViolationResponse);

    }
}
