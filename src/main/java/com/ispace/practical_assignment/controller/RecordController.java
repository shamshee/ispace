package com.ispace.practical_assignment.controller;


import com.ispace.practical_assignment.model.ApiResponse;
import com.ispace.practical_assignment.model.RecordDTO;
import com.ispace.practical_assignment.security.payload.MessageResponse;
import com.ispace.practical_assignment.service.RecordService;
import com.ispace.practical_assignment.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecordController {

    @Autowired
    private RecordService service;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveRecord(@Valid @RequestBody RecordDTO recordDTO){
        RecordDTO record=service.saveRecord(recordDTO);

       return ResponseUtil.created(record,"Record Created Successfully");
    }

    @GetMapping("/nocontent")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> noContent(){
       return ResponseUtil.error(HttpStatus.NO_CONTENT.value(), "no Content");
    }

    @GetMapping("/echo/{recordId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getRecord(@PathVariable Long recordId){
        RecordDTO record=service.getRecord(recordId);
         return ResponseUtil.success(record,"Record Fetched Successfully");
    }
    @GetMapping("/device/{recordId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getDevice(@PathVariable Long recordId) {
        String deviceId = service.getDevice(recordId);
        Map<String, String> response = new HashMap<>();
        response.put("deviceId", deviceId);
         return ResponseUtil.success(response,"Record Fetched Successfully");
    }

}
