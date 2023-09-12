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
import java.util.Optional;

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
        Balance balance = modelMapper.map(request, Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        balanceRepository.findByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(b.getUpdatedAt());
        });

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved, BalanceDTO.Response.class);
    }

    /**
     * 대출 잔고 수정 (집행 수정과 연결됨)
     */
    @Override
    public BalanceDTO.Response update(Long applicationId,
                                      BalanceDTO.UpdateRequest request) {
        // 신청건에 해당하는 잔고 존재 여부 검증 (잔고가 존재하지 않는 경우, 에러 발생)
        Balance balance = balanceRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_BALANCE));

        // as-is -> to-be (balance entryAmount update)
        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = balance.getBalance();

        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);
        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated, BalanceDTO.Response.class);
    }
}
