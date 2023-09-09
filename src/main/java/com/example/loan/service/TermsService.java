package com.example.loan.service;

import com.example.loan.dto.TermsDTO;

public interface TermsService {
    TermsDTO.Response createTerms(TermsDTO.Request request);
}
