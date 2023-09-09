package com.example.loan.controller;

import com.example.loan.dto.ResponseDTO;
import com.example.loan.dto.TermsDTO;
import com.example.loan.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermsController extends AbstractController {
    private final TermsService termsService;

    @PostMapping
    public ResponseDTO<TermsDTO.Response> createTerms(
            @RequestBody TermsDTO.Request request) {
        return ok(termsService.createTerms(request));
    }

    @GetMapping
    public ResponseDTO<List<TermsDTO.Response>> getAllTerms() {
        return ok(termsService.getAllTerms());
    }

}
