package com.ispace.practical_assignment.security.payload;


import lombok.*;


@Data
@AllArgsConstructor
//@NoArgsConstructor
public class MessageResponse {
    private int statusCode;
private String message;




}