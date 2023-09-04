package com.example.loan.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.loan.domain.Counsel;
import com.example.loan.dto.CounselDTO;
import com.example.loan.repository.CounselRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class CounselServiceTest {

    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    @DisplayName("대출 상담 등록 서비스")
    void Should_ReturnResponseOfNewCounselEntity_When_RequestCounsel() {
        //given
        Counsel entity = Counsel.builder()
                .name("Member Yu")
                .cellPhone("010-1111-2222")
                .email("test1234@gmailc.com")
                .memo("저는 대출을 받고 싶습니다. 연락 주시길 바랍니다.")
                .address("서울특별시 xx구 xx동")
                .addressDetail("101동 101호")
                .zipCode("12345")
                .build();

        CounselDTO.Request request = CounselDTO.Request.builder()
                .name("Member Yu")
                .cellPhone("010-1111-2222")
                .email("test1234@gmailc.com")
                .memo("저는 대출을 받고 싶습니다. 연락 주시길 바랍니다.")
                .address("서울특별시 xx구 xx동")
                .addressDetail("101동 101호")
                .zipCode("12345")
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class)))
                .thenReturn(entity);

        //when
        CounselDTO.Response response = counselService.create(request);

        //then
        assertThat(response.getName()).isSameAs(entity.getName());
    }
}
