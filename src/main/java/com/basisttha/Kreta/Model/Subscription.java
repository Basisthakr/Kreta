package com.basisttha.Kreta.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID subscriptionId;
    @OneToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "subscription_userId", nullable = false)
    private Users user;
    @ManyToOne
    @JoinColumn(name = "subscription_planId", nullable = false)
    private Plan plan;//nullable = false as free plan will have an entry in subscriptions as FREE
    @Column(unique = true)
    private String stripeSubscriptionId;//nullable as free plan user will not have this
    private String subscriptionStatus;//Stripe's own enum:"active", "past_due", "canceled", "unpaid", "trialing"
    //this will be set to active for free plan users manually
    private LocalDateTime currentPeriodStart;//null for free plan
    private LocalDateTime currentPeriodEnd;//stripe sends in webhook
    private Boolean cancelAtPeriodEnd;//stripe sends in webhook
    private LocalDateTime gracePeriodExpiresAt;//grace period 3 days from the time the bill was due
    private Boolean accessSuspended;//if grace period ended and payment didnt go through, local field
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp//could also use @PreUpdate
    private LocalDateTime updatedAt;//when a subscription changes
}
