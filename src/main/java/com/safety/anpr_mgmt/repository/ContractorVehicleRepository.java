package com.safety.anpr_mgmt.repository;

import com.safety.anpr_mgmt.entity.ContractorVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractorVehicleRepository extends JpaRepository<ContractorVehicleEntity, Long> {
    Optional<ContractorVehicleEntity> findTopByVehicleNoOrderBySafetyApprovalDateDesc(String plateText);
}
