package com.example.loan.controller;

import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.FileDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.ApplicationService;
import com.example.loan.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

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
     * 대출 신청 서류 업로드
     */
    @PostMapping("/{applicationId}/files")
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file)
            throws IllegalStateException {
        fileStorageService.save(applicationId, file);
        return ok();
    }

    /**
     * 신청 서류 다운로드
     */
    @GetMapping("/{applicationId}/files")
    public ResponseEntity<Resource> download (
            @PathVariable Long applicationId,
            @RequestParam(value = "fileName") String fileName) throws IllegalStateException {
        Resource file = fileStorageService.load(applicationId, fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    /**
     * 대출 신청 서류 조회
     */
    @GetMapping("/{applicationId}/files/info")
    public ResponseDTO<List<FileDTO>> getFileInfo(@PathVariable Long applicationId) {
        List<FileDTO> fileInfos = fileStorageService.loadAll(applicationId).map(path -> {
            String fileName = path.getFileName().toString();
            return FileDTO.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder
                            .fromMethodName(ApplicationController.class,
                                    "download", applicationId, fileName)
                            .build().toString()).build();
        }).collect(Collectors.toList());

        return ok(fileInfos);
    }

    /**
     * 대출 신청 서류 전체 삭제
     */
    @DeleteMapping("/{applicationId}/files")
    public ResponseDTO<Void> deleteAll(@PathVariable Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return ok();
    }

    /**
     * 대출 계약 체결
     */
    @PutMapping("/{applicationId}/contract")
    public ResponseDTO<ApplicationDTO.Response> contract(
            @PathVariable Long applicationId) {
        applicationService.contract(applicationId);
        return ok();
    }

}
