package com.example.loan.service;

import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.JudgmentDTO;

public interface JudgmentService {

    JudgmentDTO.Response create(JudgmentDTO.Request request);

    JudgmentDTO.Response getJudgment(Long judgmentId);

    JudgmentDTO.Response getJudgmentOfApplication(Long applicationId);

    JudgmentDTO.Response updateJudgment(Long judgmentId, JudgmentDTO.Request request);

    void deleteJudgment(Long judgmentId);

    ApplicationDTO.GrantAmount grant(Long judgmentId);
}
