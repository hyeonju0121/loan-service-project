package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Entry;
import com.example.loan.domain.Repayment;
import com.example.loan.dto.BalanceDTO;
import com.example.loan.dto.RepaymentDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.EntryRepository;
import com.example.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;

    private final ApplicationRepository applicationRepository;

    private final EntryRepository entryRepository;

    private final BalanceService balanceService;

    private final ModelMapper modelMapper;

    /**
     * 대출금 상환
     */
    @Override
    public RepaymentDTO.Response create(Long applicationId, RepaymentDTO.Request request) {
        // validation
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);

        // 잔고 업데이트
        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                        .repaymentAmount(request.getRepaymentAmount())
                        .build());

        RepaymentDTO.Response response =
                modelMapper.map(repayment, RepaymentDTO.Response.class);
        // 남은 대출금 잔액
        response.setBalance(updatedBalance.getBalance());

        return response;
    }

    /**
     * 대출금 상환 내역 조회
     */
    @Override
    public List<RepaymentDTO.ListResponse> getRepayments(Long applicationId) {
        // validation
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Repayment> repayments =
                repaymentRepository.findAllByApplicationId(applicationId);

        return repayments.stream().map(
                r -> modelMapper.map(r, RepaymentDTO.ListResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * 대출금 상환 수정
     */
    @Override
    public RepaymentDTO.UpdateResponse updateRepayment(
            Long repaymentId, RepaymentDTO.Request request) {

        Repayment repayment = repaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        // 대출 상환금 rollback
        balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                        .build());

        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(repayment);

        // 대출 잔고를 수정된 상환금으로 처리
        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                        .build());

        return RepaymentDTO.UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(request.getRepaymentAmount())
                .balance(updatedBalance.getBalance())
                .createdAt(repayment.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 대출금 상환 삭제
     */
    @Override
    public void deleteRepayment(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        // 삭제된 대출 상환금을 잔고에서 rollback 처리
        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();
        balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                        .build());

        // 대출 상환 정보 삭제
        repayment.setIsDeleted(true);
        repaymentRepository.save(repayment);
    }

    private boolean isRepayableApplication(Long applicationId) {

        Optional<Application> application =
                applicationRepository.findById(applicationId);

        // 대출 신청 존재여부 검증
        if (application.isEmpty()) return false;

        // 계약을 완료한 신청 정보인지
        if (application.get().getContractedAt() == null) return false;

        // 집행이 완료된 신청 정보인지
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);
        return entry.isPresent();
    }
}
