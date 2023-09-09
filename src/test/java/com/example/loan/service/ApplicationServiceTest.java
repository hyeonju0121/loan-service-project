package com.example.loan.service;

import com.example.loan.domain.AcceptTerms;
import com.example.loan.domain.Application;
import com.example.loan.domain.Terms;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.AcceptTermsRepository;
import com.example.loan.repository.ApplicationRepository;
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
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

    @Mock
    private TermsRepository termsRepository;

    @Mock
    private AcceptTermsRepository acceptTermsRepository;

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

    @Test
    @DisplayName("대출 신청 삭제 서비스")
    void Should_DeletedApplicationEntity_When_RequestExistApplicationInfo() {
        //given
        Long applicationId = 1L;

        Application entity = Application.builder()
                .applicationId(1L)
                .build();

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(entity));
        when(applicationRepository.save(ArgumentMatchers.any(Application.class)))
                .thenReturn(entity);

        //when
        applicationService.deleteApplication(applicationId);

        //then
        assertThat(entity.getIsDeleted()).isEqualTo(true);
    }

    @Test
    @DisplayName("사용자 이용 약관 동의 서비스")
    void Should_AddAcceptTerms_When_RequestAcceptTermsOfApplication() {
        //given
        Long applicationId = 1L;

        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용약관 1")
                .termsDetailUrl("http://testtest.asfddf/sdfd")
                .build();
        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용약관 2")
                .termsDetailUrl("http://testtest.asfddf/sdfdfffdfd")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 2L);
        ApplicationDTO.AcceptTerms request = ApplicationDTO.AcceptTerms.builder()
                        .acceptTermsIds(acceptTerms).build();


        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(Application.builder().build()));

        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId")))
                .thenReturn(Arrays.asList(entityA, entityB));

        when(acceptTermsRepository.save(ArgumentMatchers.any(AcceptTerms.class)))
                .thenReturn(AcceptTerms.builder().build());

        //when
        boolean result = applicationService.acceptTerms(applicationId, request);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("모든 약관에 동의하지 않은 경우 - 약관 동의 실패 케이스")
    void Should_ThrowException_When_RequestNotAllAcceptTermsOfApplication() {
        //given
        Long applicationId = 1L;

        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용약관 1")
                .termsDetailUrl("http://testtest.asfddf/sdfd")
                .build();
        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용약관 2")
                .termsDetailUrl("http://testtest.asfddf/sdfdfffdfd")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L);
        ApplicationDTO.AcceptTerms request = ApplicationDTO.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms).build();


        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(Application.builder().build()));

        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId")))
                .thenReturn(Arrays.asList(entityA, entityB));

        //when
        BaseException exception = assertThrows(BaseException.class,
                () -> applicationService.acceptTerms(applicationId, request));

        //then
        assertEquals(ResultType.NOT_ACCEPT_ALL_TERMS, exception.getErrorCode());
    }

    @Test
    @DisplayName("존재하지 않는 약관에 동의한 경우 - 약관 동의 실패 케이스")
    void Should_ThrowException_When_RequestNotExistAcceptTermsOfApplication() {
        //given
        Long applicationId = 1L;

        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용약관 1")
                .termsDetailUrl("http://testtest.asfddf/sdfd")
                .build();
        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용약관 2")
                .termsDetailUrl("http://testtest.asfddf/sdfdfffdfd")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 3L);
        ApplicationDTO.AcceptTerms request = ApplicationDTO.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms).build();


        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(Application.builder().build()));

        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId")))
                .thenReturn(Arrays.asList(entityA, entityB));

        //when
        BaseException exception = assertThrows(BaseException.class,
                () -> applicationService.acceptTerms(applicationId, request));

        //then
        assertEquals(ResultType.ACCEPT_NOT_EXIST_TERMS, exception.getErrorCode());
    }

}
