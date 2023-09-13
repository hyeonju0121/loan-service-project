package com.example.loan.service;

import com.example.loan.dto.RepaymentDTO;

import java.util.List;

public interface RepaymentService {
    RepaymentDTO.Response create(Long applicationId, RepaymentDTO.Request request);

    List<RepaymentDTO.ListResponse> getRepayments(Long applicationId);

    RepaymentDTO.UpdateResponse updateRepayment(Long repaymentId, RepaymentDTO.Request request);

    void deleteRepayment(Long repaymentId);
}
