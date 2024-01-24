package com.cedarmeadowmeats.orderservice.config;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SquareConfig {

    @Bean
    public SquareClient squareClient() {
        return new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken("ACCESS_TOKEN")
                .build();
    }

}
