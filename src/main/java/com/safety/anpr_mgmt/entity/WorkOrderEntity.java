package com.safety.anpr_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class WorkOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "work_order_no")
    private String workOrderNo;

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

    @Column(name = "name")
    private String name;

}
