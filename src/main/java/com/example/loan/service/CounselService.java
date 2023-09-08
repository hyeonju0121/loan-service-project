package com.example.loan.service;

import com.example.loan.dto.CounselDTO;

public interface CounselService {

    CounselDTO.Response create(CounselDTO.Request request);

    CounselDTO.Response getCounsel(Long counselId);

    CounselDTO.Response updateCounsel(Long counselId, CounselDTO.Request request);
}
