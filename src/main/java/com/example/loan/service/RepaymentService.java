package com.example.loan.service;

import com.example.loan.dto.RepaymentDTO;

public interface RepaymentService {
    RepaymentDTO.Response create(Long applicationId, RepaymentDTO.Request request);
}
