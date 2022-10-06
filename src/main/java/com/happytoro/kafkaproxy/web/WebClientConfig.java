package com.happytoro.kafkaproxy.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value(value = "${tokenUserService.portNumber}")
    private String tokenUserServicePortNumber;
    
    @Bean
    public WebClient createWebClient() {
        return WebClient.builder().baseUrl("http://localhost:" + tokenUserServicePortNumber)
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
