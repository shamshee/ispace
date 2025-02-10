package com.ispace.practical_assignment.security.service;

import com.ispace.practical_assignment.security.payload.LoginRequest;
import com.ispace.practical_assignment.security.payload.MessageResponse;
import com.ispace.practical_assignment.security.payload.SignupRequest;
import com.ispace.practical_assignment.security.payload.UserInfoResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<MessageResponse> signUp(SignupRequest request);
    ResponseEntity<?> signIn(LoginRequest loginRequest);
}
