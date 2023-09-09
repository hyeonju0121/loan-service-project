package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {
    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    @DisplayName("대출 신청 등록 서비스")
    void Should_ReturnResponseOfNewApplicationEntity_When_RequestCreateApplication() {
        //given
        Application entity = Application.builder()
                .name("Member Yu")
                .cellPhone("010-1111-2222")
                .email("test1234@gmailc.com")
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        ApplicationDTO.Request request = ApplicationDTO.Request.builder()
                .name("Member Yu")
                .cellPhone("010-1111-2222")
                .email("test1234@gmailc.com")
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class)))
                .thenReturn(entity);

        //when
        ApplicationDTO.Response response = applicationService.create(request);

        //then
        assertThat(response.getHopeAmount()).isEqualTo(entity.getHopeAmount());
        assertThat(response.getName()).isEqualTo(entity.getName());
    }

    @Test
    @DisplayName("대출 신청 조회 서비스")
    void Should_ReturnResponseOfExistsApplicationEntity_When_RequestExistApplicationId() {
        //given
        Long applicationId = 1L;

        Application entity = Application.builder()
                .applicationId(1L)
                .name("Member Yu")
                .build();

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(entity));

        //when
        ApplicationDTO.Response response = applicationService.getApplication(applicationId);

        //then
        assertThat(response.getApplicationId()).isEqualTo(entity.getApplicationId());
        assertThat(response.getName()).isEqualTo(entity.getName());
    }

    @Test
    @DisplayName("존재하지 않는 대출 신청 아이디인 경우 - 대출 신청 조회 실패")
    void Should_ThrowException_When_RequestNotExistApplicationId() {
        //given
        Long applicationId = 1L;

        when(applicationRepository.findById(applicationId))
                .thenThrow(new BaseException(ResultType.NOT_FOUND_APPLICATION));

        //when
        BaseException exception = assertThrows(BaseException.class,
                () -> applicationService.getApplication(applicationId));

        //then
        assertEquals(ResultType.NOT_FOUND_APPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("대출 신청 수정 서비스")
    void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestUpdateExistApplicationInfo() {
        //given
        Long applicationId = 1L;

        Application entity = Application.builder()
                .applicationId(1L)
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        ApplicationDTO.Request request = ApplicationDTO.Request.builder()
                .hopeAmount(BigDecimal.valueOf(80000000))
                .build();

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(entity));

        when(applicationRepository.save(ArgumentMatchers.any(Application.class)))
                .thenReturn(entity);

        //when
        ApplicationDTO.Response response = applicationService.updateApplication(
                applicationId, request);

        //then
        assertThat(response.getHopeAmount()).isEqualTo(entity.getHopeAmount());
        assertThat(response.getApplicationId()).isEqualTo(applicationId);
    }


}
