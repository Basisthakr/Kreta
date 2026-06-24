package com.basisttha.Kreta.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
