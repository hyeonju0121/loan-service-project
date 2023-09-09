package com.example.loan.controller;

import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.ApplicationService;
import com.example.loan.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController extends AbstractController {

    private final ApplicationService applicationService;

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseDTO<ApplicationDTO.Response> createApplication(
            @RequestBody ApplicationDTO.Request request) {
        return ok(applicationService.create(request));
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<ApplicationDTO.Response> getApplication(
            @PathVariable Long applicationId) {
        return ok(applicationService.getApplication(applicationId));
    }

    @PutMapping("/{applicationId}")
    public ResponseDTO<ApplicationDTO.Response> updateApplication(
            @PathVariable Long applicationId,
            @RequestBody ApplicationDTO.Request request) {
        return ok(applicationService.updateApplication(applicationId, request));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> deleteApplication(
            @PathVariable Long applicationId) {
        applicationService.deleteApplication(applicationId);
        return ok();
    }

    @PostMapping("/{applicationId}/terms")
    public ResponseDTO<Boolean> acceptTerms(@PathVariable Long applicationId,
                            @RequestBody ApplicationDTO.AcceptTerms request) {
        return ok(applicationService.acceptTerms(applicationId, request));
    }

    /**
     * 서류 업로드
     */
    @PostMapping("/files")
    public ResponseDTO<Void> upload(MultipartFile file) {
        fileStorageService.save(file);
        return ok();
    }

}
