package com.cedarmeadowmeats.orderservice.config;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SquareConfig {

    @Value("${order-service.square.access-token:}")
    private String squareSandboxAccessKey;

    @Bean
    public SquareClient squareClient() {
        return new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(squareSandboxAccessKey)
                .build();
    }



}
