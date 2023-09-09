package com.example.loan.service;

import com.example.loan.dto.ApplicationDTO;

public interface ApplicationService {

    ApplicationDTO.Response create(ApplicationDTO.Request request);

    ApplicationDTO.Response getApplication(Long applicationId);

    ApplicationDTO.Response updateApplication(
            Long applicationId, ApplicationDTO.Request request);

    void deleteApplication(Long applicationId);
}
