package com.example.loan.controller;

import com.example.loan.dto.CounselDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counsels")
public class CounselController extends AbstractController {

    private final CounselService counselService;

    @PostMapping
    public ResponseDTO<CounselDTO.Response> createCounsel(
            @RequestBody CounselDTO.Request request) {
        return ok(counselService.create(request));
    }

    @GetMapping("/{counselId}")
    public ResponseDTO<CounselDTO.Response> getCounsel(@PathVariable Long counselId) {
        return ok(counselService.getCounsel(counselId));
    }
}
