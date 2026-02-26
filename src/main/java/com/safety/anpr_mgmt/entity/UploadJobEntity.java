package com.safety.anpr_mgmt.entity;


import com.safety.anpr_mgmt.enums.UploadJobStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UploadJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="job_id")
    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UploadJobStatus status;

    @Column(name = "message")
    private String message;

}
