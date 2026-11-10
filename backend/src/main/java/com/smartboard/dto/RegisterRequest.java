package com.smartboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Must be a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    // min=8: NIST SP 800-63B baseline. max=72: BCrypt silently truncates beyond 72 bytes;
    // capping here prevents silent truncation and blocks large-payload DoS attempts.
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    private String password;
}
