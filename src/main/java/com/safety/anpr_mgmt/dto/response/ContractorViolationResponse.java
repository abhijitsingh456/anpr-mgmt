package com.safety.anpr_mgmt.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ContractorViolationResponse {


    private Long id;

    //VIOLATION DETAILS
    private String violationId;
    private LocalDateTime dateTime;
    private String plateText;
    private BigDecimal speedObserved;
    private String location;
    //


    // VEHICLE DETAILS
    private String vehicleType;
    private String name;
    private String ownerName;
    private String transporterName;
    private String supervisorName;
    private String workOrderNo;
    private String heavyVehicleGatePassNo;
    private LocalDate safetyApprovalDate;
    private String cisfApprovalDate;


    //WORK ORDER DETAILS
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private String vendorCode;
    private String engineerIncharge;
    private String department;
    private String vendorName;

}
