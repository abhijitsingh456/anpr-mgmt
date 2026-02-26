package com.safety.anpr_mgmt.repository;

import com.safety.anpr_mgmt.entity.UploadJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadJobRepository extends JpaRepository<UploadJobEntity, Long> {

}
