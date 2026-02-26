package com.safety.anpr_mgmt.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ContractorVehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "vehicle_no")
    private String vehicleNo;

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

}
