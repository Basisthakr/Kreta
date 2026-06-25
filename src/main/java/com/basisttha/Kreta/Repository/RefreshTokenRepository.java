package com.basisttha.Kreta.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.RefreshToken;
import com.basisttha.Kreta.Model.Users;

import java.util.List;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID>{
    Optional<RefreshToken> findByRefreshTokenAndIsRevokedFalse(UUID refreshToken);
    List<RefreshToken> findByOwnerAndIsRevokedFalse(Users user);
}
