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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    @Test
    @DisplayName("약관 전체 조회 서비스")
    void Should_ReturnAllResponseOfExistTermsEntities_When_RequestTermsList() {
        //given
        Terms entityA = Terms.builder()
                        .name("대출 이용약관 1")
                        .termsDetailUrl("http://testtest.asfddf/sdfd")
                        .build();
        Terms entityB = Terms.builder()
                .name("대출 이용약관 2")
                .termsDetailUrl("http://testtest.asfddf/sdfdfffdfd")
                .build();

        List<Terms> termsList = new ArrayList<>(Arrays.asList(entityA, entityB));

        when(termsRepository.findAll())
                .thenReturn(termsList);

        //when
        List<TermsDTO.Response> response = termsService.getAllTerms();

        //then
        assertThat(response.size()).isEqualTo(termsList.size());
    }

}
