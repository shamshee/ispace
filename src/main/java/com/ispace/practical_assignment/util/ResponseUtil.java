package com.ispace.practical_assignment.util;


import com.ispace.practical_assignment.model.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    private ResponseUtil() {
        // Prevent instantiation
    }

    public static <T> ResponseEntity<com.ispace.practical_assignment.model.ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(200, message, data, false));
    }

    public static <T> ResponseEntity<com.ispace.practical_assignment.model.ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(201).body(new ApiResponse<>( 201, message, data, false));
    }

    public static ResponseEntity<ApiResponse<Void>> success(String message) {
        return ResponseEntity.ok(new ApiResponse<>(200, message, false));
    }

    public static ResponseEntity<ApiResponse<Void>> error(int statusCode, String message) {
        return ResponseEntity.status(statusCode).body(new ApiResponse<>(statusCode, message, true));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(int statusCode, String message, T data) {
        return ResponseEntity.status(statusCode).body(new ApiResponse<>(statusCode, message, data, true));
    }
}
