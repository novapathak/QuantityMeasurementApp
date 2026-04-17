package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.config.GoogleOAuth2Properties;
import com.apps.quantitymeasurement.exception.ApiErrorResponse;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.model.AppUserEntity;
import com.apps.quantitymeasurement.model.AuthRequest;
import com.apps.quantitymeasurement.model.AuthResponse;
import com.apps.quantitymeasurement.model.RegisterRequest;
import com.apps.quantitymeasurement.security.JwtService;
import com.apps.quantitymeasurement.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation or registration error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        AppUserEntity user = appUserService.registerLocalUser(registerRequest);
        String token = jwtService.generateToken(user.getUsername(), user.getRoles(), buildTokenClaims(user));
        return buildAuthResponse(user, token);
    }

    @Operation(summary = "Authenticate with username and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials payload", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        AppUserEntity user = appUserService.getByIdentifier(authentication.getName());
        String token = jwtService.generateToken(user.getUsername(), user.getRoles(), buildTokenClaims(user));
        return buildAuthResponse(user, token);
    }

    @Operation(summary = "Get currently authenticated principal")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated user details", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
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
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirects to Google OAuth2 authorization endpoint"),
            @ApiResponse(responseCode = "400", description = "OAuth2 is not enabled", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/google")
    public ResponseEntity<Void> googleLogin() {
        if (!googleOAuth2Properties.isEnabled()) {
            throw new QuantityMeasurementException("Google OAuth2 is not enabled");
        }

        URI target = UriComponentsBuilder.fromPath("/oauth2/authorization/google").build().toUri();
        return ResponseEntity.status(HttpStatus.FOUND).location(target).build();
    }

    private Map<String, Object> buildTokenClaims(AppUserEntity user) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("authProvider", user.getAuthProvider().name());
        claims.put("email", user.getEmail());
        claims.put("name", user.getFullName());
        return claims;
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
                List.copyOf(user.getRoles())
        );
    }
}
