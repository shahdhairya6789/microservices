package org.example.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
