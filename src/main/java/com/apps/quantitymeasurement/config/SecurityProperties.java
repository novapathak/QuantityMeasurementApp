package com.apps.quantitymeasurement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private Jwt jwt = new Jwt();
    private DefaultUser defaultUser = new DefaultUser();
    private Cors cors = new Cors();

    @Data
    public static class Jwt {
        private String secret = "change-this-jwt-secret-to-a-long-random-value-for-production-1234567890";
        private long expirationMs = 3600000;
        private String issuer = "quantity-measurement-app";
    }

    @Data
    public static class DefaultUser {
        private boolean enabled = true;
        private String username = "admin";
        private String email = "admin@quantitymeasurement.local";
        private String fullName = "Default Admin";
        private String password = "Admin@12345";
        private List<String> roles = new ArrayList<>(List.of("USER"));
    }

    @Data
    public static class Cors {
        private List<String> allowedOrigins = new ArrayList<>(List.of(
                "http://localhost:8080",
                "http://quantitymeasurementapp-production-56d8.up.railway.app",
                "http://127.0.0.1:8080"
        ));
        private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        private List<String> allowedHeaders = new ArrayList<>(List.of("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        private List<String> exposedHeaders = new ArrayList<>(List.of("Authorization"));
        private boolean allowCredentials = true;
    }
}
