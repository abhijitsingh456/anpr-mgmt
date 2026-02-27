package com.safety.anpr_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_violation_id",
                        columnNames = {"violation_id"}
                )
        })
public class ContractorViolationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    //VIOLATION DETAILS
    @Column(name = "violation_id")
    private String violationId;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "plate_text")
    private String plateText;

    @Column(name = "speed_observed")
    private BigDecimal speedObserved;

    @Column(name = "location")
    private String location;
    //


    // VEHICLE DETAILS
    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "name")
    private String name;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "transporter_name")
    private String transporterName;

    @Column(name = "supervisor_name")
    private String supervisorName;

    @Column(name = "work_order_no")
    private String workOrderNo;

    @Column(name = "heavy_vehicle_gate_pass_no")
    private String heavyVehicleGatePassNo;

    @Column(name = "safety_approval_date")
    private LocalDate safetyApprovalDate;

    @Column(name = "cisf_approval_date")
    private String cisfApprovalDate; //String because many vehicles don't have it


    //WORK ORDER DETAILS
    @Column(name = "validity_start_date")
    private LocalDate validityStartDate;

    @Column(name = "validity_end")
    private LocalDate validityEndDate;

    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "engineer_incharge")
    private String engineerIncharge;

    @Column(name = "department")
    private String department;

    @Column(name = "service_location")
    private String serviceLocation;

    @Column(name = "short_text")
    private String shortText;

    @Column(name = "net_order_value")
    private String netOrderValue;

    @Column(name = "vendor_name")
    private String vendorName;

}
