package com.phegondev.adminuserpanel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Tüm yollar (/**) için CORS kurallarını tanımlar
                registry.addMapping("/**")
                        // Tüm kaynaklardan gelen isteklere izin verir (Geliştirme için)
                        .allowedOrigins("*")
                        // Tüm HTTP metotlarına izin verir
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        // Tüm başlıkların kullanılmasına izin verir (Authorization gibi)
                        .allowedHeaders("*");
            }
        };
    }
}