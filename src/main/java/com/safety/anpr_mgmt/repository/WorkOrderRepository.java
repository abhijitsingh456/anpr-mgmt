package com.safety.anpr_mgmt.repository;

import com.safety.anpr_mgmt.entity.WorkOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrderEntity, Long> {

}
