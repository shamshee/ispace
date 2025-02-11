package com.ispace.practical_assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private boolean error;
    private LocalDateTime timestamp;

    public ApiResponse(int statusCode, String message, boolean error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(int statusCode, String message, T data, boolean error) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
