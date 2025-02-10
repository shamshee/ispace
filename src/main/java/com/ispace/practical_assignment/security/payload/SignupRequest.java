package com.ispace.practical_assignment.security.payload;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 40)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email(message = "Please Provide valid email id")
    private String email;


    private Set<String> role;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,40}$",
            message = "Password must be alphanumeric and contain at least one special character.")
    private String password;

}