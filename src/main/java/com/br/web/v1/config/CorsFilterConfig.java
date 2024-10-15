package com.br.web.v1.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsFilterConfig {

    public static final List<String> allowedOrigins = Arrays.asList("http://localhost:4200");

    @Bean
    public FilterRegistrationBean<CorsFilter> initCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        config.addAllowedMethod("*");
        config.setAllowedOrigins(allowedOrigins);

        //Configuração para API de login
        UrlBasedCorsConfigurationSource authConfigSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration authConfig = new CorsConfiguration();
        authConfig.setAllowCredentials(true);
        authConfig.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        authConfig.addAllowedMethod("*");
        authConfig.setAllowedOrigins(allowedOrigins);
        authConfigSource.registerCorsConfiguration("/auth/login", authConfig);

        //Configuração para API de validação de token já existente
        UrlBasedCorsConfigurationSource tokenValid = new UrlBasedCorsConfigurationSource();
        CorsConfiguration tokenValidConfig = new CorsConfiguration();
        tokenValidConfig.setAllowCredentials(true);
        tokenValidConfig.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        tokenValidConfig.addAllowedMethod("*");
        tokenValidConfig.setAllowedOrigins(allowedOrigins);
        tokenValid.registerCorsConfiguration("/auth/tokenValid", authConfig);


        source.registerCorsConfiguration("/**", config);
        source.registerCorsConfiguration("/auth/login", authConfig);
        source.registerCorsConfiguration("/auth/tokenValid", tokenValidConfig);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}