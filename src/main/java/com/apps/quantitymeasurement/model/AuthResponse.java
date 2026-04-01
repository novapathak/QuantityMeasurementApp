package com.apps.quantitymeasurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private Instant expiresAt;
    private String username;
    private String email;
    private String fullName;
    private String authProvider;
    private List<String> authorities;
}
