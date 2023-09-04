package com.example.loan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.loan.domain.Counsel;
import com.example.loan.dto.CounselDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
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

import java.util.Optional;

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

    @Test
    @DisplayName("대출 상담 조회 서비스")
    void Should_ReturnResponseOfExistsCounselEntity_When_RequestExistCounselId() {
        //given
        Long counselId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        when(counselRepository.findById(counselId))
                .thenReturn(Optional.ofNullable(entity));

        //when
        CounselDTO.Response response = counselService.getCounsel(counselId);

        //then
        assertThat(response.getCounselId()).isSameAs(counselId);
    }

    @Test
    @DisplayName("존재하지 않는 상담 아이디인 경우 - 대출 상담 조회 실패")
    void Should_ThrowException_When_RequestNotExistCounselId() {
        //given
        Long counselId = 1L;

        when(counselRepository.findById(counselId))
                .thenThrow(new BaseException(ResultType.SYSTEM_ERROR));

        //when
        BaseException exception = assertThrows(BaseException.class,
                () -> counselService.getCounsel(counselId));

        //then
        assertEquals(ResultType.SYSTEM_ERROR, exception.getErrorCode());
    }
}
