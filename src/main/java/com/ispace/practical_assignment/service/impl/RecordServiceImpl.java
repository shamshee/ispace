package com.ispace.practical_assignment.service.impl;

import com.ispace.practical_assignment.entity.RecordEntity;
import com.ispace.practical_assignment.exception.ResourceNotFoundException;
import com.ispace.practical_assignment.model.RecordDTO;
import com.ispace.practical_assignment.repository.RecordRepository;
import com.ispace.practical_assignment.service.RecordService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;


@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private ModelMapper mapper;


    @Autowired
    private RecordRepository repository;


    // Custom configuration to prevent ModelMapper to  mapping 'deviceId' to 'id'
//    @PostConstruct
//    public void configureModelMapper() {
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        mapper.typeMap(RecordDTO.class, RecordEntity.class).addMappings(mapping ->
//                mapping.skip(RecordEntity::setRecordId));  // Skip 'id' field mapping
//    }


    @Override
    public RecordDTO saveRecord(RecordDTO recordDTO) {
        RecordEntity entity=mapper.map(recordDTO,RecordEntity.class);
        RecordEntity savedEntity=repository.save(entity);

        return mapper.map(savedEntity,RecordDTO.class);
    }

    @Override
    public RecordDTO getRecord(Long recordId) {
        RecordEntity persistedEntity= repository.findById(recordId)
                .orElseThrow(ResourceNotFoundException::new);
        return mapper.map(persistedEntity,RecordDTO.class);
    }

    @Override
    public String getDevice(Long recordId) {
        RecordEntity persistedEntity= repository.findById(recordId)
                .orElseThrow(ResourceNotFoundException::new);
        return persistedEntity.getDeviceId();
    }
}
