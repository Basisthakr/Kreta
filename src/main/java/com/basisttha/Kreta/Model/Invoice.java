package com.basisttha.Kreta.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    private String stripeInvoiceId;//unique, not null, provided by Stripe, why not UUID?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId_invoice")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_for_subscription_id")
    private Subscription subscription;
    private Long amountDuePaise;//total amount stripe tried to charge
    private Long amountPaidPaise;//amount collected. 0 if failed
    private String status;//"paid", "open", "void", "uncollectible"
    private LocalDateTime dueDate;
    private LocalDateTime paidAt;
    private String hostedInvoiceUrl;//stripe gives a link for URL
    private LocalDateTime createdAt;

    @PrePersist
    void setStuff(){
        this.createdAt = LocalDateTime.now();
    }
}
