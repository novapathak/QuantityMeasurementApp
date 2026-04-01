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
}
