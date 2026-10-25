package com.example.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {
    @Bean
    RestClient productRestClient(
            RestClient.Builder builder,
            @Value("${product.service.url}") String productServiceUrl) {
        return builder.baseUrl(productServiceUrl).build();
    }
}
