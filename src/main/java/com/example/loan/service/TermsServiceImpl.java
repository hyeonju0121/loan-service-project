package com.example.loan.service;

import com.example.loan.domain.Terms;
import com.example.loan.dto.TermsDTO;
import com.example.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {
    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;

    /**
     * 약관 등록
     */
    @Override
    public TermsDTO.Response createTerms(TermsDTO.Request request) {
        Terms terms = modelMapper.map(request, Terms.class);
        Terms created = termsRepository.save(terms);

        return modelMapper.map(created, TermsDTO.Response.class);
    }

    /**
     * 약관 전체 조회
     */
    @Override
    public List<TermsDTO.Response> getAllTerms() {
        List<Terms> termsList = termsRepository.findAll();

        return termsList.stream().map(
                t -> modelMapper.map(t, TermsDTO.Response.class))
                .collect(Collectors.toList());
    }

}
