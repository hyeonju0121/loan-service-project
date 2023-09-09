package com.example.loan.service;

import com.example.loan.domain.Terms;
import com.example.loan.dto.TermsDTO;
import com.example.loan.repository.TermsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TermsServiceTest {
    @InjectMocks
    TermsServiceImpl termsService;

    @Mock
    private TermsRepository termsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    @DisplayName("약관 등록 서비스")
    void Should_ReturnResponseOfNewTermsEntity_When_RequestCreateTerms() {
        //given
        Terms entity = Terms.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/djkfkgkfk")
                .build();

        TermsDTO.Request request = TermsDTO.Request.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/djkfkgkfk")
                .build();

        when(termsRepository.save(ArgumentMatchers.any(Terms.class)))
                .thenReturn(entity);

        //when
        TermsDTO.Response response = termsService.createTerms(request);

        //then
        assertThat(response.getName()).isEqualTo(entity.getName());
        assertThat(response.getTermsDetailUrl()).isEqualTo(entity.getTermsDetailUrl());
    }


}
