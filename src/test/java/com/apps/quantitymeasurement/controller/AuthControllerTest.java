package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.config.GoogleOAuth2Properties;
import com.apps.quantitymeasurement.exception.GlobalExceptionHandler;
import com.apps.quantitymeasurement.model.AppUserEntity;
import com.apps.quantitymeasurement.model.AuthProvider;
import com.apps.quantitymeasurement.model.AuthResponse;
import com.apps.quantitymeasurement.model.RegisterRequest;
import com.apps.quantitymeasurement.security.JwtService;
import com.apps.quantitymeasurement.service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private GoogleOAuth2Properties googleOAuth2Properties;

    @MockBean
    private AppUserService appUserService;

    @Test
    void testRegisterSuccess() throws Exception {
        AppUserEntity user = appUser("new-user", "new-user@example.com", "New User");

        when(appUserService.registerLocalUser(any(RegisterRequest.class))).thenReturn(user);
        when(jwtService.generateToken(eq("new-user"), any(), any())).thenReturn("registered-token");
        when(jwtService.extractExpiration("registered-token")).thenReturn(Instant.parse("2030-01-01T00:00:00Z"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest(
                                "new-user",
                                "new-user@example.com",
                                "New User",
                                "StrongPass123"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("registered-token"))
                .andExpect(jsonPath("$.username").value("new-user"))
                .andExpect(jsonPath("$.email").value("new-user@example.com"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        AppUserEntity user = appUser("admin", "admin@quantitymeasurement.local", "Default Admin");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(appUserService.getByIdentifier("admin")).thenReturn(user);
        when(jwtService.generateToken(eq("admin"), any(), any())).thenReturn("jwt-token");
        when(jwtService.extractExpiration("jwt-token")).thenReturn(Instant.parse("2030-01-01T00:00:00Z"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of(
                                "username", "admin",
                                "password", "Admin@12345"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.authProvider").value("LOCAL"));
    }

    @Test
    void testGoogleLoginRedirect() throws Exception {
        when(googleOAuth2Properties.isEnabled()).thenReturn(true);

        mockMvc.perform(get("/api/v1/auth/google"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/oauth2/authorization/google"));
    }

    private AppUserEntity appUser(String username, String email, String fullName) {
        AppUserEntity user = new AppUserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setRoles(new LinkedHashSet<>(List.of("ROLE_USER")));
        user.setEnabled(true);
        return user;
    }
}
