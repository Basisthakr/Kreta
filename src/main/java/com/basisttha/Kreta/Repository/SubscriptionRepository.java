package com.basisttha.Kreta.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID>{
    
}
