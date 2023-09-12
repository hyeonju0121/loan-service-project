package com.example.loan.service;

import com.example.loan.dto.EntryDTO;

public interface EntryService {

    EntryDTO.Response create(Long applicationId, EntryDTO.Request request);
}
