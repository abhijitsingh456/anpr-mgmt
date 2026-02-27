package com.safety.anpr_mgmt.repository;

import com.safety.anpr_mgmt.entity.ContractorViolationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorViolationRepository extends JpaRepository<ContractorViolationEntity, Long> {

}
