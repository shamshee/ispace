package com.ispace.practical_assignment.service;

import com.ispace.practical_assignment.model.RecordDTO;

public interface RecordService {
    RecordDTO saveRecord(RecordDTO recordDTO);
    RecordDTO getRecord(Long recordId);
    String getDevice(Long recordId);
}
