package org.example.myspringsecurity.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @NotBlank(message = "Email is mandatory")
        @NotEmpty(message = "Email is mandatory")
        @Email(message = "Email format is wrong")
        String email,
        @NotBlank(message = "Password is mandatory")
        @NotEmpty(message = "Password is mandatory")
        @Size(min = 4,message = "Min size is 4")
        String password
) {
}

