package com.example.loan.controller;

import com.example.loan.dto.EntryDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController {

    private final EntryService entryService;

    /**
     * 대출 집행
     */
    @PostMapping("/{applicationId}/entries")
    public ResponseDTO<EntryDTO.Response> create(
            @PathVariable Long applicationId, @RequestBody EntryDTO.Request request) {
        return ok(entryService.create(applicationId, request));
    }
}
