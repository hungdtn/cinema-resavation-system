//package org.example.cinema_reservation_system.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").permitAll() // GET: ai cũng được
//                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasAnyRole("ADMIN", "CLIENT", "USER") // ✅ POST: cho ADMIN, CLIENT hoặc USER
//                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN") // PUT: chỉ ADMIN
//                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN") // DELETE: chỉ ADMIN
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
//        return http.build();
//    }
//
//}
