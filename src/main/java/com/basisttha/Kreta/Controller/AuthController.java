package com.basisttha.Kreta.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basisttha.Kreta.DTO.AuthResponse;
import com.basisttha.Kreta.DTO.LoginRequest;
import com.basisttha.Kreta.DTO.RefreshTokenRequest;
import com.basisttha.Kreta.DTO.RefreshTokenResponse;
import com.basisttha.Kreta.DTO.RegisterRequest;
import com.basisttha.Kreta.Service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req){
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req){
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshJwt(@Valid @RequestBody RefreshTokenRequest req){
        return ResponseEntity.ok(authService.refreshJWT(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        authService.logout();
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }
}
