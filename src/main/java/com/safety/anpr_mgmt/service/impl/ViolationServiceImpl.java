package com.safety.anpr_mgmt.service.impl;

import com.safety.anpr_mgmt.dto.response.ContractorViolationResponse;
import com.safety.anpr_mgmt.entity.ContractorViolationEntity;
import com.safety.anpr_mgmt.repository.ContractorViolationRepository;
import com.safety.anpr_mgmt.service.ViolationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ViolationServiceImpl implements ViolationService {

    private final ContractorViolationRepository contractorViolationRepository;

    @Override
    public List<ContractorViolationResponse> getContractorViolations(LocalDateTime startDateTime, LocalDateTime endDateTime){

        List<ContractorViolationEntity> violationEntities = contractorViolationRepository.findByDateTimeBetween(startDateTime, endDateTime);

        return convertToContractorViolationResponse(violationEntities);

    }

    private List<ContractorViolationResponse> convertToContractorViolationResponse(List<ContractorViolationEntity> violationEntities){

        return violationEntities.stream()
                .map(violationEntity -> {
                    ContractorViolationResponse response = new ContractorViolationResponse();
                    response.setId(violationEntity.getId());
                    response.setViolationId(violationEntity.getViolationId());
                    response.setDateTime(violationEntity.getDateTime());
                    response.setPlateText(violationEntity.getPlateText());
                    response.setSpeedObserved(violationEntity.getSpeedObserved());
                    response.setLocation(violationEntity.getLocation());
                    response.setVehicleType(violationEntity.getVehicleType());
                    response.setName(violationEntity.getName());
                    response.setOwnerName(violationEntity.getOwnerName());
                    response.setTransporterName(violationEntity.getTransporterName());
                    response.setSupervisorName(violationEntity.getSupervisorName());
                    response.setWorkOrderNo(violationEntity.getWorkOrderNo());
                    response.setHeavyVehicleGatePassNo(violationEntity.getHeavyVehicleGatePassNo());
                    response.setSafetyApprovalDate(violationEntity.getSafetyApprovalDate());
                    response.setCisfApprovalDate(violationEntity.getCisfApprovalDate());
                    response.setValidityStartDate(violationEntity.getValidityStartDate());
                    response.setValidityEndDate(violationEntity.getValidityEndDate());
                    response.setVendorCode(violationEntity.getVendorCode());
                    response.setEngineerIncharge(violationEntity.getEngineerIncharge());
                    response.setDepartment(violationEntity.getDepartment());
                    response.setVendorName(violationEntity.getVendorName());
                    return response;
                })
                .toList();

    }
}
