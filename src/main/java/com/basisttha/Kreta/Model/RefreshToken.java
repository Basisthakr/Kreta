package com.basisttha.Kreta.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID refreshTokenId;
    private UUID refreshToken;
    @ManyToOne
    @JoinColumn(name = "owner_refreshToken", nullable = false)
    private Users owner;
    private LocalDateTime expiry;
    @CreationTimestamp
    private LocalDateTime issuedAt;
    private Boolean isRevoked;
}
