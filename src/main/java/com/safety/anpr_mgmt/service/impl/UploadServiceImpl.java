package com.safety.anpr_mgmt.service.impl;

import com.safety.anpr_mgmt.dto.response.UploadJobResponse;
import com.safety.anpr_mgmt.entity.ContractorVehicleEntity;
import com.safety.anpr_mgmt.entity.UploadJobEntity;
import com.safety.anpr_mgmt.entity.WorkOrderEntity;
import com.safety.anpr_mgmt.enums.UploadJobStatus;
import com.safety.anpr_mgmt.repository.ContractorVehicleRepository;
import com.safety.anpr_mgmt.repository.UploadJobRepository;
import com.safety.anpr_mgmt.repository.WorkOrderRepository;
import com.safety.anpr_mgmt.service.UploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final UploadJobRepository uploadJobRepository;
    private final ContractorVehicleRepository contractorVehicleRepository;
    private final WorkOrderRepository workOrderRepository;

    @Override
    public UploadJobResponse getStatus(Long jobId){

        UploadJobEntity uploadJob = uploadJobRepository.findById(jobId).orElseThrow();

        UploadJobResponse uploadJobResponse = new UploadJobResponse();
        uploadJobResponse.setJobId(uploadJob.getJobId());
        uploadJobResponse.setStatus(uploadJob.getStatus());
        uploadJobResponse.setMessage(uploadJob.getMessage());

        return uploadJobResponse;
    }


    @Override
    public UploadJobResponse uploadVehicle(MultipartFile file){

        try{
            Path tempFile = Files.createTempFile("upload-", ".csv");
            file.transferTo(tempFile);

            UploadJobEntity uploadJob = new UploadJobEntity();
            uploadJob.setStatus(UploadJobStatus.PROCESSING);

            UploadJobEntity job = uploadJobRepository.save(uploadJob);

            vehicleFileProcessing(job.getJobId(), tempFile);

            UploadJobResponse uploadJobResponse = new UploadJobResponse();
            uploadJobResponse.setJobId(job.getJobId());
            uploadJobResponse.setStatus(UploadJobStatus.PROCESSING);
            uploadJobResponse.setMessage("");

            return uploadJobResponse;
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public UploadJobResponse uploadWorkOrder(MultipartFile file){

        try{
            Path tempFile = Files.createTempFile("upload-", ".csv");
            file.transferTo(tempFile);

            UploadJobEntity uploadJob = new UploadJobEntity();
            uploadJob.setStatus(UploadJobStatus.PROCESSING);

            UploadJobEntity job = uploadJobRepository.save(uploadJob);

            workOrderFileProcessing(job.getJobId(), tempFile);

            UploadJobResponse uploadJobResponse = new UploadJobResponse();
            uploadJobResponse.setJobId(job.getJobId());
            uploadJobResponse.setStatus(UploadJobStatus.PROCESSING);
            uploadJobResponse.setMessage("");

            return uploadJobResponse;
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    @Async
    @Transactional
    private void vehicleFileProcessing(Long jobId, Path filePath){

        UploadJobEntity uploadJob = uploadJobRepository.findById(jobId).orElseThrow();

        try(
                BufferedReader reader = Files.newBufferedReader(filePath, Charset.forName("Windows-1252"));
                CSVParser csvParser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setTrim(true)
                        .build().parse(reader)
        ){

            List<ContractorVehicleEntity> batch = new ArrayList<>();
            int batchSize = 1000;
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd-MM-yyyy");

            for(CSVRecord cols: csvParser){

                //if no safety approval date found, do not add to DB
                if(cols.get(36).isEmpty()){
                    continue;
                }

                ContractorVehicleEntity vehicle = new ContractorVehicleEntity();
                vehicle.setVehicleNo(cols.get(0));
                vehicle.setVehicleType(cols.get(1));
                vehicle.setName(cols.get(3));
                vehicle.setOwnerName(cols.get(15));
                vehicle.setTransporterName(cols.get(21));
                vehicle.setSupervisorName(cols.get(27));
                vehicle.setWorkOrderNo(cols.get(30));
                vehicle.setHeavyVehicleGatePassNo(cols.get(33));
                vehicle.setSafetyApprovalDate(LocalDate.parse(cols.get(36),formatter));
                vehicle.setCisfApprovalDate(cols.get(37));

                batch.add(vehicle);

                if(batch.size() == batchSize){
                    contractorVehicleRepository.saveAll(batch);
                    batch.clear();
                }

            }

            if(!batch.isEmpty()){
                contractorVehicleRepository.saveAll(batch);
            }

            uploadJob.setStatus(UploadJobStatus.COMPLETED);

        } catch (Exception e){
            uploadJob.setStatus(UploadJobStatus.FAILED);
            uploadJob.setMessage(e.getMessage());
        }

        uploadJobRepository.save(uploadJob);
    }

    @Async
    @Transactional
    private void workOrderFileProcessing(Long jobId, Path filePath){

        UploadJobEntity uploadJob = uploadJobRepository.findById(jobId).orElseThrow();

        try(
                BufferedReader reader = Files.newBufferedReader(filePath, Charset.forName("Windows-1252"));
                CSVParser csvParser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setTrim(true)
                        .build().parse(reader)
        ){

            List<WorkOrderEntity> batch = new ArrayList<>();
            int batchSize = 1000;
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd-MM-yyyy");

            for(CSVRecord cols: csvParser){

                WorkOrderEntity workOrder = new WorkOrderEntity();

                workOrder.setWorkOrderNo(cols.get(0));
                workOrder.setValidityStartDate(LocalDate.parse(cols.get(3),formatter));
                workOrder.setValidityEndDate(LocalDate.parse(cols.get(4),formatter));
                workOrder.setVendorCode(cols.get(5));
                workOrder.setEngineerIncharge(cols.get(9));
                workOrder.setDepartment(cols.get(12));
                workOrder.setServiceLocation(cols.get(14));
                workOrder.setShortText(cols.get(17));
                workOrder.setNetOrderValue(cols.get(20));
                workOrder.setName(cols.get(25));

                batch.add(workOrder);

                if(batch.size() == batchSize){
                    workOrderRepository.saveAll(batch);
                    batch.clear();
                }

            }

            if(!batch.isEmpty()){
                workOrderRepository.saveAll(batch);
            }

            uploadJob.setStatus(UploadJobStatus.COMPLETED);

        } catch (Exception e){
            uploadJob.setStatus(UploadJobStatus.FAILED);
            uploadJob.setMessage(e.getMessage());
        }

        uploadJobRepository.save(uploadJob);
    }

}
