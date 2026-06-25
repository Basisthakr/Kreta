package com.basisttha.Kreta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.WebhookEvent;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, String>{
    
}
