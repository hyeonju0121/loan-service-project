package com.example.loan.controller;

import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.JudgmentDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.JudgmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{judgmentId}")
    public ResponseDTO<JudgmentDTO.Response> getJudgment(
            @PathVariable Long judgmentId) {
        return ok(judgmentService.getJudgment(judgmentId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<JudgmentDTO.Response> getJudgmentOfApplicationId(
            @PathVariable Long applicationId) {
        return ok(judgmentService.getJudgmentOfApplication(applicationId));
    }

    @PutMapping("/{judgmentId}")
    public ResponseDTO<JudgmentDTO.Response> updateJudgment(
            @PathVariable Long judgmentId, @RequestBody JudgmentDTO.Request request) {
        return ok(judgmentService.updateJudgment(judgmentId, request));
    }

    @DeleteMapping("/{judgmentId}")
    public ResponseDTO<Void> deleteJudgment(@PathVariable Long judgmentId) {
        judgmentService.deleteJudgment(judgmentId);
        return ok();
    }

    @PatchMapping("/{judgmentId}/grants")
    public ResponseDTO<ApplicationDTO.GrantAmount> grant(
            @PathVariable Long judgmentId) {
        return ok(judgmentService.grant(judgmentId));
    }

}
