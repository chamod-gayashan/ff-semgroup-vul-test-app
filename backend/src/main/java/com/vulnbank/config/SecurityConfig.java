package com.vulnbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // VULN-ID: VB-011 | CWE-16 | Severity: HIGH
    // Security misconfiguration: CSRF disabled, wildcard CORS, frame options disabled,
    // all requests permitted without authentication.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(List.of("*"));
                cfg.setAllowedMethods(List.of("*"));
                cfg.setAllowedHeaders(List.of("*"));
                return cfg;
            }))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
        // SAFE VERSION:
        // http.csrf(AbstractHttpConfigurer::enable)
        //     .cors(cors -> cors.configurationSource(request -> {
        //         CorsConfiguration cfg = new CorsConfiguration();
        //         cfg.setAllowedOrigins(List.of("https://vulnbank.internal"));
        //         cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        //         return cfg;
        //     }))
        //     .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny))
        //     .authorizeHttpRequests(auth -> auth
        //         .requestMatchers("/api/auth/**").permitAll()
        //         .anyRequest().authenticated());
    }
}
