package com.basisttha.Kreta.Model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {//This table only changes when I change the plans
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID planId;//local, not from stripe
    @NotNull
    private String planName;
    private String stripePriceIdMonthly;
    private String stripePriceIdYearly;
    private Long amountMonthlyPaise;
    private Long amountYearlyPaise;
    private Integer apiCallLimit;
    private Integer rateLimitPerMinute;
    private String stripeMeteredPriceId;
    private Integer maxApiKeys;//amount of api keys this tier allows
    @ElementCollection//makes a separate table for features called plan_features, as one plan only has one row in Plan table, but features is a list
    @CollectionTable(name = "plan_features", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "feature")
    private List<String> features;
}
