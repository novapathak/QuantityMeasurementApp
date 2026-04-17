package com.apps.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @Schema(description = "JWT access token", nullable = true)
    private String accessToken;
    @Schema(example = "Bearer")
    private String tokenType;
    @Schema(description = "JWT expiration timestamp", nullable = true)
    private Instant expiresAt;
    private String username;
    private String email;
    private String fullName;
    private String authProvider;
    private List<String> authorities;
}
