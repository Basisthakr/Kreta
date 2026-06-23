package com.basisttha.Kreta.Initialization;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.basisttha.Kreta.Model.Plan;
import com.basisttha.Kreta.Repository.PlanRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PlanRepository planRepository;

    @Value("${stripe.price.bronze.monthly}") private String bronzeMonthly;
    @Value("${stripe.price.bronze.yearly}") private String bronzeYearly;
    @Value("${stripe.price.bronze.metered}") private String bronzeMetered;

    @Value("${stripe.price.silver.monthly}") private String silverMonthly;
    @Value("${stripe.price.silver.yearly}") private String silverYearly;
    @Value("${stripe.price.silver.metered}") private String silverMetered;

    @Value("${stripe.price.gold.monthly}") private String goldMonthly;
    @Value("${stripe.price.gold.yearly}") private String goldYearly;
    @Value("${stripe.price.gold.metered}") private String goldMetered;

    @Override
    public void run(ApplicationArguments args) {
        if (planRepository.count() > 0) {
            return;
        }

        List<Plan> plans = List.of(
            Plan.builder()
                .planName("FREE")
                .stripePriceIdMonthly(null)
                .stripePriceIdYearly(null)
                .stripeMeteredPriceId(null)
                .amountMonthlyPaise(0L)
                .amountYearlyPaise(0L)
                .apiCallLimit(1000)
                .rateLimitPerMinute(10)
                .maxApiKeys(2)
                .features(List.of("1,000 API calls per month", "2 API keys", "Community support"))
                .build(),

            Plan.builder()
                .planName("BRONZE")
                .stripePriceIdMonthly(bronzeMonthly)
                .stripePriceIdYearly(bronzeYearly)
                .stripeMeteredPriceId(bronzeMetered)
                .amountMonthlyPaise(49900L)
                .amountYearlyPaise(499900L)
                .apiCallLimit(50000)
                .rateLimitPerMinute(60)
                .maxApiKeys(5)
                .features(List.of("50,000 API calls per month", "5 API keys", "Email support"))
                .build(),

            Plan.builder()
                .planName("SILVER")
                .stripePriceIdMonthly(silverMonthly)
                .stripePriceIdYearly(silverYearly)
                .stripeMeteredPriceId(silverMetered)
                .amountMonthlyPaise(149900L)
                .amountYearlyPaise(1499900L)
                .apiCallLimit(500000)
                .rateLimitPerMinute(300)
                .maxApiKeys(20)
                .features(List.of("500,000 API calls per month", "20 API keys", "Priority support"))
                .build(),

            Plan.builder()
                .planName("GOLD")
                .stripePriceIdMonthly(goldMonthly)
                .stripePriceIdYearly(goldYearly)
                .stripeMeteredPriceId(goldMetered)
                .amountMonthlyPaise(499900L)
                .amountYearlyPaise(4999900L)
                .apiCallLimit(5000000)
                .rateLimitPerMinute(1000)
                .maxApiKeys(100)
                .features(List.of("5 Million API calls per month", "100 API keys", "Dedicated support"))
                .build()
        );

        planRepository.saveAll(plans);
    }
}