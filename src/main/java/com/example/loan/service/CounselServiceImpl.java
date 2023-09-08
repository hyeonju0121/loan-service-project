package com.example.loan.service;

import com.example.loan.domain.Counsel;
import com.example.loan.dto.CounselDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {
    private final ModelMapper modelMapper;
    private final CounselRepository counselRepository;
    @Override
    public CounselDTO.Response create(CounselDTO.Request request) {
        Counsel counsel = modelMapper.map(request, Counsel.class);
        counsel.setAppliedAt(LocalDateTime.now());

        Counsel created = counselRepository.save(counsel);

        return modelMapper.map(created, CounselDTO.Response.class);
    }

    @Override
    public CounselDTO.Response getCounsel(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        return modelMapper.map(counsel, CounselDTO.Response.class);
    }

    @Override
    public CounselDTO.Response updateCounsel(
            Long counselId, CounselDTO.Request request) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        counsel.setName(request.getName());
        counsel.setCellPhone(request.getCellPhone());
        counsel.setEmail(request.getEmail());
        counsel.setMemo(request.getMemo());
        counsel.setAddress(request.getAddress());
        counsel.setAddressDetail(request.getAddressDetail());
        counsel.setZipCode(request.getZipCode());

        counselRepository.save(counsel);

        return modelMapper.map(counsel, CounselDTO.Response.class);
    }

    @Override
    public void deleteCounsel(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        counsel.setIsDeleted(true);

        counselRepository.save(counsel);
    }

}
