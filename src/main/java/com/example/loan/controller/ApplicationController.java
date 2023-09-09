package com.example.loan.controller;

import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController extends AbstractController {

    private final ApplicationService applicationService;

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

}
