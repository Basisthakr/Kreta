package com.basisttha.Kreta.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct//Runs after Spring has injected value fields into this bean, but before any controller receives any request
    void init(){
        Stripe.apiKey = stripeSecretKey;
    }
    
}
