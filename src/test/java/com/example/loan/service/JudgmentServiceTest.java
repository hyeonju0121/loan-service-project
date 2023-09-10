package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Judgment;
import com.example.loan.dto.JudgmentDTO;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JudgmentServiceTest {
    @InjectMocks
    private JudgmentServiceImpl judgmentService;

    @Mock
    private JudgmentRepository judgmentRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    @DisplayName("대출 심사 등록 서비스")
    void Should_ReturnResponseOfNewJudgmentEntity_When_RequestNewJudgment() {
        //given
        JudgmentDTO.Request request = JudgmentDTO.Request.builder()
                .applicationId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        Judgment judgment = Judgment.builder()
                .applicationId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(Application.builder().build()));

        when(judgmentRepository.save(ArgumentMatchers.any(Judgment.class)))
                .thenReturn(judgment);

        //when
        JudgmentDTO.Response response = judgmentService.create(request);

        //then
        assertThat(response.getName()).isEqualTo(judgment.getName());
        assertThat(response.getApplicationId()).isEqualTo(request.getApplicationId());
        assertThat(response.getApprovalAmount()).isEqualTo(request.getApprovalAmount());
    }

    @Test
    @DisplayName("대출 심사 조회 서비스 - 대출 심사 아이디")
    void Should_ReturnResponseOfJudgmentEntity_When_RequestExistJudgmentId() {
        //given
        Long judgmentId = 1L;

        Judgment judgment = Judgment.builder()
                .judgmentId(1L)
                .applicationId(2L)
                .build();

        when(judgmentRepository.findById(judgmentId))
                .thenReturn(Optional.of(judgment));

        //when
        JudgmentDTO.Response response = judgmentService.getJudgment(judgmentId);

        //then
        assertThat(response.getJudgmentId()).isEqualTo(judgment.getJudgmentId());
        assertThat(response.getApplicationId()).isEqualTo(judgment.getApplicationId());
    }

    @Test
    @DisplayName("대출 심사 조회 서비스 - 대출 신청 아이디")
    void Should_ReturnResponseOfJudgmentEntity_When_RequestExistApplicationId() {
        //given
        Judgment judgment = Judgment.builder()
                .judgmentId(1L)
                .applicationId(2L)
                .build();

        Application application = Application.builder()
                .applicationId(2L)
                .build();


        when(applicationRepository.findById(2L))
                .thenReturn(Optional.of(Application.builder().build()));

        when(judgmentRepository.findByApplicationId(2L))
                .thenReturn(Optional.of(judgment));
        //when
        JudgmentDTO.Response response = judgmentService.getJudgmentOfApplication(2L);

        //then
        assertThat(response.getApplicationId()).isEqualTo(judgment.getApplicationId());
        assertThat(response.getJudgmentId()).isEqualTo(judgment.getJudgmentId());
    }


}
