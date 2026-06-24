package com.basisttha.Kreta.DTO;

import java.util.UUID;

import com.basisttha.Kreta.Model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO for both login and registration
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UUID refreshToken;
    private String email;
    private Role role;
}
