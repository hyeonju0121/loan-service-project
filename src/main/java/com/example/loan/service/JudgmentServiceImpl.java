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

    /**
     * 대출 심사 조회 - (대출 심사 아이디로 조회)
     */
    @Override
    public JudgmentDTO.Response getJudgment(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_JUDGMENT));

        return modelMapper.map(judgment, JudgmentDTO.Response.class);
    }

    /**
     * 대출 심사 조회 - (대출 신청 아이디로 조회)
     */
    @Override
    public JudgmentDTO.Response getJudgmentOfApplication(Long applicationId) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.NOT_FOUND_APPLICATION);
        }

        Judgment judgment = judgmentRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_JUDGMENT));
        return modelMapper.map(judgment, JudgmentDTO.Response.class);
    }

    /**
     * 대출 심사 수정
     */
    @Override
    public JudgmentDTO.Response updateJudgment(
            Long judgmentId, JudgmentDTO.Request request) {

        Judgment judgment = judgmentRepository.findById(judgmentId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_JUDGMENT));

        judgment.setName(request.getName());
        judgment.setApprovalAmount(request.getApprovalAmount());

        judgmentRepository.save(judgment);

        return modelMapper.map(judgment, JudgmentDTO.Response.class);
    }

    /**
     * 대출 심사 삭제
     */
    @Override
    public void deleteJudgment(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId)
                .orElseThrow(() -> new BaseException(ResultType.NOT_FOUND_JUDGMENT));

        judgment.setIsDeleted(true);

        judgmentRepository.save(judgment);
    }


    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }
}
