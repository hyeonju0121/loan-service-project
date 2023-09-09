package com.example.loan.service;

import com.example.loan.dto.TermsDTO;

import java.util.List;

public interface TermsService {
    TermsDTO.Response createTerms(TermsDTO.Request request);

    List<TermsDTO.Response> getAllTerms();
}
