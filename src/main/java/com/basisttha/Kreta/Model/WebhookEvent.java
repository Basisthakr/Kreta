package com.basisttha.Kreta.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEvent {
    @Id
    private String stripeEventId;//unique, not null, provided by Stripe
    private String eventType;
    private LocalDateTime processedAt;
    private Status status;
    private String errorMessage;
}