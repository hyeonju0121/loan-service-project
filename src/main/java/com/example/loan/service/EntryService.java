package com.example.loan.service;

import com.example.loan.dto.EntryDTO;

public interface EntryService {

    EntryDTO.Response create(Long applicationId, EntryDTO.Request request);

    EntryDTO.Response getEntry(Long applicationId);

    EntryDTO.UpdateResponse updateEntry(Long entryId, EntryDTO.Request request);

    void deleteEntry(Long entryId);
}
