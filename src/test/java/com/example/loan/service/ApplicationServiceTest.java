package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.dto.ApplicationDTO;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

}
