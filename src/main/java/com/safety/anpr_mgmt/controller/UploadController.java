package com.safety.anpr_mgmt.controller;

import com.safety.anpr_mgmt.dto.response.UploadJobResponse;
import com.safety.anpr_mgmt.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping(value = "/vehicles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadJobResponse> uploadVehicles(@RequestParam("file") MultipartFile file){

        UploadJobResponse uploadJobResponse = uploadService.uploadVehicle(file);

        return ResponseEntity.accepted().body(uploadJobResponse);
    }

    @GetMapping("/status/{job_id}")
    public ResponseEntity<UploadJobResponse> getStatus(@PathVariable Long job_id){

        UploadJobResponse uploadJobResponse = uploadService.getStatus(job_id);

        return ResponseEntity.ok().body(uploadJobResponse);

    }
}
