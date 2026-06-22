package com.basisttha.Kreta.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.RevokedToken;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, UUID>{
    Optional<RevokedToken> findByToken(String token);
}
