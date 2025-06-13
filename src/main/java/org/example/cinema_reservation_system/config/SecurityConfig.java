package org.example.cinema_reservation_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/rap/**").hasRole("ADMIN")
                                .requestMatchers("/api/rap/**").permitAll()
//                        .requestMatchers("/api/phim/**").hasAnyRole("ADMIN", "MANAGER") //cần login
                        .requestMatchers("/uploads/**","/api/phim/**").permitAll() // ko cần login
                        .requestMatchers("/api/phong-chieu/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic();
        return http.build();
    }
}
