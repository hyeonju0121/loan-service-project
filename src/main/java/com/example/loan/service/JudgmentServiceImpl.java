package com.example.loan.service;

import com.example.loan.domain.Judgment;
import com.example.loan.dto.JudgmentDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JudgmentServiceImpl implements JudgmentService {

    private final JudgmentRepository judgmentRepository;

    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;

    /**
     * 대출 심사 등록
     */
    @Override
    public JudgmentDTO.Response create(JudgmentDTO.Request request) {
        Long applicationId = request.getApplicationId();

        // 신청 정보 검증
        if(!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.NOT_FOUND_APPLICATION);
        }

        Judgment judgment = modelMapper.map(request, Judgment.class);
        Judgment saved = judgmentRepository.save(judgment);

        return modelMapper.map(saved, JudgmentDTO.Response.class);
    }


    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }


}
