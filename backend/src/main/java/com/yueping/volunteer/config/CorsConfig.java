package com.yueping.volunteer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedOriginPatterns;

    public CorsConfig(@Value("${app.cors.allowed-origins:}") String allowedOrigins) {
        this.allowedOriginPatterns = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toArray(String[]::new);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration mapping = registry.addMapping("/api/**");
        if (allowedOriginPatterns.length > 0) {
            mapping.allowedOriginPatterns(allowedOriginPatterns);
        }
        mapping.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
