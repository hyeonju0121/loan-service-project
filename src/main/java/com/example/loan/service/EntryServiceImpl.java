package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Entry;
import com.example.loan.dto.BalanceDTO;
import com.example.loan.dto.EntryDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final BalanceService balanceService;

    private final EntryRepository entryRepository;

    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;

    /**
     * 대출 집행 등록
     */
    @Override
    public EntryDTO.Response create(Long applicationId, EntryDTO.Request request) {
        // 계약 체결 여부 검증
        if (!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.NOT_FOUND_CONTRACT);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);

        entryRepository.save(entry);

        // 대출 잔고 관리 (집행된 금액을 잔고에 업데이트)
        balanceService.create(applicationId,
                BalanceDTO.Request.builder()
                        .applicationId(applicationId)
                        .entryAmount(request.getEntryAmount())
                        .build());

        return modelMapper.map(entry, EntryDTO.Response.class);
    }

    /**
     * 유효성 검증 메서드
     */
    private boolean isContractedApplication(Long applicationId) {
        // 신청 정보 존재여부 검증
        Optional<Application> application =
                applicationRepository.findById(applicationId);

        if (application.isEmpty()) return false;

        return application.get().getContractedAt() != null;
    }
}
