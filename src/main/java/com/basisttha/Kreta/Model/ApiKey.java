package com.basisttha.Kreta.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID keyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApiKey_User")
    private Users user;
    private String keyHash; //Api key hash, SHA-256 as it is faster than Bcrypt
    private String prefix;//first 10 chars of the API key, stored in plaintext, for user to verify 
    private Boolean isActive;//if false, means revoked
    private LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;

    @PrePersist
    void setStuff(){
        this.createdAt = LocalDateTime.now();
    }
}
