package com.safety.anpr_mgmt.repository;

import com.safety.anpr_mgmt.entity.WorkOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrderEntity, Long> {
    Optional<WorkOrderEntity> findByWorkOrderNo(String workOrderNo);
}
