package com.ispace.practical_assignment.controller;


import com.ispace.practical_assignment.model.RecordDTO;
import com.ispace.practical_assignment.security.payload.MessageResponse;
import com.ispace.practical_assignment.service.RecordService;
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
    public ResponseEntity<RecordDTO> saveRecord(@Valid @RequestBody RecordDTO recordDTO){
        RecordDTO record=service.saveRecord(recordDTO);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @GetMapping("/nocontent")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> noContent(){
        return new ResponseEntity<>("no content",HttpStatus.NO_CONTENT);
    }

    @GetMapping("/echo/{recordId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<RecordDTO> getRecord(@PathVariable Long recordId){
        RecordDTO record=service.getRecord(recordId);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/device/{recordId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> getDevice(@PathVariable Long recordId) {
        String deviceId = service.getDevice(recordId);
        Map<String, String> response = new HashMap<>();
        response.put("deviceId", deviceId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/**")
    public ResponseEntity<MessageResponse> handleInvalidPath(HttpServletRequest request) {

        String path=request.getRequestURI();
        MessageResponse errorResponse = new MessageResponse(
                HttpStatus.NOT_FOUND.value(),
                "The requested path ("+path+") does not exist"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
