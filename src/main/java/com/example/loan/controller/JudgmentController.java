package com.example.loan.controller;

import com.example.loan.dto.JudgmentDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.JudgmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/judgments")
public class JudgmentController extends AbstractController {

    private final JudgmentService judgmentService;

    @PostMapping
    public ResponseDTO<JudgmentDTO.Response> create(
            @RequestBody JudgmentDTO.Request request) {
        return ok(judgmentService.create(request));
    }
}
