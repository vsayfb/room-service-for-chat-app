package com.example.room_service.config;

import com.example.room_service.argument_resolver.ClientSessionArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed_origins:*}")
    private String allowedOrigin;

    private final ClientSessionArgumentResolver clientSessionArgumentResolver;

    public WebConfig(ClientSessionArgumentResolver clientSessionArgumentResolver) {
        this.clientSessionArgumentResolver = clientSessionArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(clientSessionArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/rooms/").allowedMethods("GET", "POST").allowedOrigins(allowedOrigin);
        registry.addMapping("/rooms/*").allowedMethods("GET").allowedOrigins(allowedOrigin);
        registry.addMapping("/rooms/join/*").allowedMethods("POST").allowedOrigins(allowedOrigin);
    }
}
