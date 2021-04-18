package com.moviebuzz.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig
{
    @Value("${service.users}")
    private String userServiceUrl;

    @Value("${service.frontapi}")
    private String frontApiUrl;

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(p -> p
                .path("/get*")
                .filters(f -> f.addRequestHeader("Hello", "World"))
                .uri("http://httpbin.org:80"))
            .route(p -> p
                .path("/moviebuzz/v1.0/user/**")
                .uri(userServiceUrl))
            .route(p -> p
                .path("/moviebuzz/v1.0/bookings/**", "/moviebuzz/v1.0/movies/**",
                    "/moviebuzz/v1.0/theatres/**", "/moviebuzz/v1.0/reviews/**")
                .uri(frontApiUrl))
            .build();
    }
}
