package com.safety.anpr_mgmt.service.impl;

import com.safety.anpr_mgmt.entity.ContractorVehicleEntity;
import com.safety.anpr_mgmt.entity.ContractorViolationEntity;
import com.safety.anpr_mgmt.entity.WorkOrderEntity;
import com.safety.anpr_mgmt.repository.ContractorVehicleRepository;
import com.safety.anpr_mgmt.repository.ContractorViolationRepository;
import com.safety.anpr_mgmt.repository.WorkOrderRepository;
import com.safety.anpr_mgmt.service.StreetsOnCloudService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class StreetsOnCloudServiceImpl implements StreetsOnCloudService {

    private final RestTemplate restTemplate;

    private final ContractorVehicleRepository contractorVehicleRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ContractorViolationRepository contractorViolationRepository;

    @Value("${streetsOnCloud.api-key}")
    private String apiKey;

    @Override
    @Scheduled(cron = "0 00 01 * * ?", zone = "Asia/Kolkata")
    public void fetchViolationRecords() {
        LocalDate targetDate = LocalDate.now().minusDays(1);

        log.info("Starting StreetsOnCloud fetch job for date {}", targetDate);

        LocalDateTime startDate = targetDate.atStartOfDay();
        LocalDateTime endDate = targetDate.atTime(23, 59, 59);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        String url = "https://api.streetsoncloud.com/v4/tickets-list";

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Range", "items=0-10");
        headers.set("x-api-key", apiKey);
        headers.set("X-Requested-With", "XMLHttpRequest");

        List<Map<String, Object>> finalResult = new ArrayList<>();

        for (int i = -4; i < 10; i++) {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("dateFrom", startDateStr);
            requestBody.put("dateTo", endDateStr);
            requestBody.put("exportStatus", null);
            requestBody.put("status", i);
            requestBody.put("accountId", 5574);
            requestBody.put("units", 1);
            requestBody.put("optional", true);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        Map.class
                );

                Map<String, Object> body = response.getBody();
                if (body == null) {
                    log.warn("Empty response body for status {}", i);
                    continue;
                }

                List<?> tickets = (List<?>) body.getOrDefault("tickets", List.of());

                finalResult.addAll((Collection<? extends Map<String, Object>>) tickets);

            } catch (Exception e) {
                log.error("Error fetching tickets for status {}: {}", i, e.getMessage(), e);
            }
        }

        addViolationsToDatabase(finalResult);

    }

    @Transactional
    private void addViolationsToDatabase(List<Map<String, Object>> tickets) {

        int vehicleDetailNotFound = 0;
        int workOrderDetailNotFound = 0;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Map<String, Object> ticket : tickets) {

            Optional<ContractorVehicleEntity> vehicle = contractorVehicleRepository.findTopByVehicleNoOrderBySafetyApprovalDateDesc((String) ticket.get("plateText"));
            if (vehicle.isEmpty()) {
                vehicleDetailNotFound+=1;
                continue;
            }

            Optional<WorkOrderEntity> workOrder = workOrderRepository.findByWorkOrderNo(vehicle.get().getWorkOrderNo());
            if (workOrder.isEmpty()) {
                workOrderDetailNotFound+=1;
            }

            ContractorViolationEntity violation = new ContractorViolationEntity();

            Object idObj = ticket.get("id");
            String violationId = String.valueOf(idObj);
            violation.setViolationId(violationId);

            violation.setDateTime(LocalDateTime.parse((String) ticket.get("dateTimeLocal"), dateTimeFormatter));
            violation.setPlateText((String) ticket.get("plateText"));

            Object speedObj = ticket.get("speedObserved");
            if (speedObj != null) {
                BigDecimal speed = new BigDecimal(speedObj.toString())
                        .multiply(BigDecimal.valueOf(1.609344));
                violation.setSpeedObserved(speed);
            }

            violation.setLocation((String) ticket.get("location"));
            violation.setVehicleType(vehicle.get().getVehicleType());
            violation.setName(vehicle.get().getName());
            violation.setOwnerName(vehicle.get().getOwnerName());
            violation.setTransporterName(vehicle.get().getTransporterName());
            violation.setSupervisorName(vehicle.get().getSupervisorName());
            violation.setWorkOrderNo(vehicle.get().getWorkOrderNo());
            violation.setHeavyVehicleGatePassNo(vehicle.get().getHeavyVehicleGatePassNo());
            violation.setSafetyApprovalDate(vehicle.get().getSafetyApprovalDate());
            violation.setCisfApprovalDate(vehicle.get().getCisfApprovalDate());
            if (workOrder.isPresent()) {
                violation.setValidityStartDate(workOrder.get().getValidityStartDate());
                violation.setValidityEndDate(workOrder.get().getValidityEndDate());
                violation.setVendorCode(workOrder.get().getVendorCode());
                violation.setEngineerIncharge(workOrder.get().getEngineerIncharge());
                violation.setDepartment(workOrder.get().getDepartment());
                violation.setServiceLocation(workOrder.get().getServiceLocation());
                violation.setShortText(workOrder.get().getShortText());
                violation.setNetOrderValue(workOrder.get().getNetOrderValue());
                violation.setVendorName(workOrder.get().getName());
            }

            contractorViolationRepository.save(violation);
        }

        log.info("Completed fetch and record job. Total tickets processed: {}.\n" +
                "Vehicle details not found for: {}.\n" +
                "Vehicle found but Work Order details not for: {}.\n", tickets.size(), vehicleDetailNotFound, workOrderDetailNotFound);
    }
}
