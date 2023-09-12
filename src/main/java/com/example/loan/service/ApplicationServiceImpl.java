package com.example.loan.service;

import com.example.loan.domain.AcceptTerms;
import com.example.loan.domain.Application;
import com.example.loan.domain.Judgment;
import com.example.loan.domain.Terms;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.AcceptTermsRepository;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgmentRepository;
import com.example.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;

    private final TermsRepository termsRepository;

    private final AcceptTermsRepository acceptTermsRepository;

    private final JudgmentRepository judgmentRepository;

    private final ModelMapper modelMapper;

    /**
     * 대출 신청 등록
     */
    @Override
    public ApplicationDTO.Response create(ApplicationDTO.Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);

        return modelMapper.map(applied, ApplicationDTO.Response.class);
    }

    /**
     * 대출 신청 조회
     */
    @Override
    public ApplicationDTO.Response getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_APPLICATION));

        return modelMapper.map(application, ApplicationDTO.Response.class);
    }

    /**
     * 대출 신청 수정
     */
    @Override
    public ApplicationDTO.Response updateApplication(
            Long applicationId, ApplicationDTO.Request request) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_APPLICATION));

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, ApplicationDTO.Response.class);
    }

    /**
     * 대출 신청 삭제
     */
    @Override
    public void deleteApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_APPLICATION));

        application.setIsDeleted(true);

        applicationRepository.save(application);
    }

    /**
     * 사용자 이용 약관 동의
     */
    @Override
    public Boolean acceptTerms(Long applicationId, ApplicationDTO.AcceptTerms request) {
        // 대출 신청이 존재하지 않는 경우, 에러 발생
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_APPLICATION));

        // 약관이 존재하지 않는 경우, 에러 발생
        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if (termsList.isEmpty()) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 약관 항목에 해당 약관들을 모두 동의하지 않는 경우, 에러 발생
        List<Long> acceptTermsIds = request.getAcceptTermsIds();
        if (termsList.size() != acceptTermsIds.size()) {
            throw new BaseException(ResultType.NOT_ACCEPT_ALL_TERMS);
        }

        // 사용자가 존재하지 않는 약관에 동의한 경우, 에러 발생
        List<Long> termsIds = termsList.stream().map(Terms::getTermsId)
                .collect(Collectors.toList()); // termsList 에 termsId 만 추출해서 리스트 생성
        Collections.sort(acceptTermsIds);

        if(!new HashSet<>(termsIds).containsAll(acceptTermsIds)) {
            throw new BaseException(ResultType.ACCEPT_NOT_EXIST_TERMS);
        }

        for (Long termsId : acceptTermsIds) {
            AcceptTerms accepted = AcceptTerms.builder()
                                        .termsId(termsId)
                                        .applicationId(applicationId)
                                        .build();
            acceptTermsRepository.save(accepted);
        }

        return true;
    }

    /**
     * 대출 계약 체결 기능
     */
    @Override
    public void contract(Long applicationId) {
        // 신청 정보 존재여부 검증
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_APPLICATION));

        // 이미 체결된 계약인지 여부 검증
        if (application.getContractedAt() != null) {
            throw new BaseException(ResultType.ALREADY_EXIST_CONTRACT);
        }

        // 심사 정보 존재여부 검증
        Judgment judgment = judgmentRepository.findByApplicationId(application.getApplicationId())
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_JUDGMENT));

        // 대출 승인 금액이 null 또는 0원인 경우, 에러 발생
        if (application.getApprovalAmount() == null ||
                application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new BaseException(ResultType.NOT_APPROVED);
        }

        // 계약 체결
        application.setContractedAt(LocalDateTime.now());
        applicationRepository.save(application);
    }
}
