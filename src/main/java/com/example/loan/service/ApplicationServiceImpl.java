package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    /**
     * 대출 신청 등록
     */
    @Override
    public ApplicationDTO.Response create(ApplicationDTO.Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);

        return modelMapper.map(applied, ApplicationDTO.Response.class);
    }
}
