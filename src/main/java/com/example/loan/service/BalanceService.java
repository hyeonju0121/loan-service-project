package com.example.loan.service;

import com.example.loan.dto.BalanceDTO;

public interface BalanceService {

    BalanceDTO.Response create(Long applicationId, BalanceDTO.Request request);

    BalanceDTO.Response update(Long applicationId, BalanceDTO.UpdateRequest request);
}
