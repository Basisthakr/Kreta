package com.basisttha.Kreta.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID>{
    
}
