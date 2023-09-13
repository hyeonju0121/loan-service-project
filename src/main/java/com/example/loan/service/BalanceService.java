package com.example.loan.service;

import com.example.loan.dto.BalanceDTO;

public interface BalanceService {

    BalanceDTO.Response create(Long applicationId, BalanceDTO.Request request);

    BalanceDTO.Response update(Long applicationId, BalanceDTO.UpdateRequest request);

    // 대출금이 상환됐을 때, 잔고에 반영 처리를 하기 위한 기능 수행
    BalanceDTO.Response repaymentUpdate(Long applicationId, BalanceDTO.RepaymentRequest request);
}
