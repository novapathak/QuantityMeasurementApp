package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.config.GoogleOAuth2Properties;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.model.AppUserEntity;
import com.apps.quantitymeasurement.model.AuthRequest;
import com.apps.quantitymeasurement.model.AuthResponse;
import com.apps.quantitymeasurement.model.RegisterRequest;
import com.apps.quantitymeasurement.security.JwtService;
import com.apps.quantitymeasurement.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "JWT and OAuth2 authentication endpoints")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final GoogleOAuth2Properties googleOAuth2Properties;
    private final AppUserService appUserService;

    @Operation(summary = "Register a new local user")
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        AppUserEntity user = appUserService.registerLocalUser(registerRequest);
        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRoles(),
                new java.util.LinkedHashMap<>(java.util.Map.of(
                        "authProvider", user.getAuthProvider().name(),
                        "email", user.getEmail(),
                        "name", user.getFullName()
                ))
        );
        return buildAuthResponse(user, token);
    }

    @Operation(summary = "Authenticate with username and password")
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        AppUserEntity user = appUserService.getByIdentifier(authentication.getName());
        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRoles(),
                new java.util.LinkedHashMap<>(java.util.Map.of(
                        "authProvider", user.getAuthProvider().name(),
                        "email", user.getEmail(),
                        "name", user.getFullName()
                ))
        );
        return buildAuthResponse(user, token);
    }

    @Operation(summary = "Get currently authenticated principal")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public AuthResponse me(Authentication authentication) {
        String identifier = authentication.getName();
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            identifier = userDetails.getUsername();
        }
        AppUserEntity user = appUserService.getByIdentifier(identifier);
        return new AuthResponse(
                null,
                "Bearer",
                null,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAuthProvider().name(),
                user.getRoles().stream().toList()
        );
    }

    @Operation(summary = "Start Google OAuth2 login")
    @GetMapping("/google")
    public ResponseEntity<Void> googleLogin() {
        if (!googleOAuth2Properties.isEnabled()) {
            throw new QuantityMeasurementException("Google OAuth2 is not enabled");
        }

        URI target = UriComponentsBuilder.fromPath("/oauth2/authorization/google").build().toUri();
        return ResponseEntity.status(HttpStatus.FOUND).location(target).build();
    }

    private AuthResponse buildAuthResponse(AppUserEntity user, String token) {
        return new AuthResponse(
                token,
                "Bearer",
                jwtService.extractExpiration(token),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAuthProvider().name(),
                user.getRoles().stream().toList()
        );
    }
}
