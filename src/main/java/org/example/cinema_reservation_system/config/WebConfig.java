package org.example.cinema_reservation_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = "file:///" + uploadPath.replace("\\", "/") + "/";
        System.out.println(">> Resource location: " + resourceLocation);
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")                      // Cho phép API path /api/...
                .allowedOrigins("http://localhost:5173", "http://localhost:5174"    )    // frontend chạy trên cổng 5173
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // các method cho phép
                .allowedHeaders("*")                         // cho phép tất cả header
                .allowCredentials(true);                     // cho phép gửi cookie nếu có
    }
}

