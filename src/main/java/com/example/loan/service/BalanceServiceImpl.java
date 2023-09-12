package com.example.loan.service;

import com.example.loan.domain.Balance;
import com.example.loan.dto.BalanceDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    private final ModelMapper modelMapper;

    /**
     * 대출 잔고 생성 (집행이 되면 잔고가 생성됨)
     */
    @Override
    public BalanceDTO.Response create(Long applicationId,
                                      BalanceDTO.Request request) {
        // 신청건에 해당하는 잔고 존재 여부 검증 (잔고가 존재하는 경우, 에러 발생)
        if (balanceRepository.findByApplicationId(applicationId).isPresent()) {
            throw new BaseException(ResultType.ALREADY_EXIST_BALANCE);
        }

        Balance balance = modelMapper.map(request, Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved, BalanceDTO.Response.class);
    }
}
