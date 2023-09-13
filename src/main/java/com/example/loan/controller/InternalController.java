package com.example.loan.controller;

import com.example.loan.dto.EntryDTO;
import com.example.loan.dto.RepaymentDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.EntryService;
import com.example.loan.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController {

    private final EntryService entryService;

    private final RepaymentService repaymentService;

    /**
     * 대출 집행
     */
    @PostMapping("/{applicationId}/entries")
    public ResponseDTO<EntryDTO.Response> create(
            @PathVariable Long applicationId, @RequestBody EntryDTO.Request request) {
        return ok(entryService.create(applicationId, request));
    }

    /**
     * 대출 집행 조회
     */
    @GetMapping("/{applicationId}/entries")
    public ResponseDTO<EntryDTO.Response> getEntry(@PathVariable Long applicationId) {
        return ok(entryService.getEntry(applicationId));
    }

    /**
     * 대출 집행 수정
     */
    @PutMapping("/entries/{entryId}")
    public ResponseDTO<EntryDTO.UpdateResponse> updateEntry(
            @PathVariable Long entryId, @RequestBody EntryDTO.Request request) {
        return ok(entryService.updateEntry(entryId, request));
    }

    /**
     * 대출 집행 삭제
     */
    @DeleteMapping("/entries/{entryId}")
    public ResponseDTO<Void> deleteEntry (@PathVariable Long entryId) {
        entryService.deleteEntry(entryId);
        return ok();
    }

    /**
     * 대출 상환 등록
     */
    @PostMapping("/{applicationId}/repayments")
    public ResponseDTO<RepaymentDTO.Response> create(
            @PathVariable Long applicationId,
            @RequestBody RepaymentDTO.Request request) {
        return ok(repaymentService.create(applicationId, request));
    }

    /**
     * 대출 상환 조회
     */
    @GetMapping("/{applicationId}/repayments")
    public ResponseDTO<List<RepaymentDTO.ListResponse>> getRepayments(
            @PathVariable Long applicationId) {
        return ok(repaymentService.getRepayments(applicationId));
    }

    /**
     * 대출 상환 수정
     */
    @PutMapping("/repayments/{repaymentId}")
    public ResponseDTO<RepaymentDTO.UpdateResponse> updateRepayment(
            @PathVariable Long repaymentId,
            @RequestBody RepaymentDTO.Request request) {
        return ok(repaymentService.updateRepayment(repaymentId, request));
    }

    /**
     * 대출 상환 삭제
     */
    @DeleteMapping("/repayments/{repaymentId}")
    public ResponseDTO<Void> deleteRepayment(@PathVariable Long repaymentId) {
        repaymentService.deleteRepayment(repaymentId);
        return ok();
    }
}
