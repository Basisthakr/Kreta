package com.basisttha.Kreta.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tokenId;
    
    private String token;

    private LocalDateTime expiresAt;
    private LocalDateTime revokedAt;
}
