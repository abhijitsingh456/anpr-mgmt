package com.safety.anpr_mgmt.service;

import com.safety.anpr_mgmt.dto.response.UploadJobResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    UploadJobResponse getStatus(Long jobId);
    UploadJobResponse uploadVehicle(MultipartFile file);
    //UploadJobResponse uploadWorkOrder(MultipartFile file);
}
