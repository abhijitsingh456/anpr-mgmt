package com.safety.anpr_mgmt.repository;

import com.safety.anpr_mgmt.entity.ContractorViolationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ContractorViolationRepository extends JpaRepository<ContractorViolationEntity, Long> {

    List<ContractorViolationEntity> findByDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
