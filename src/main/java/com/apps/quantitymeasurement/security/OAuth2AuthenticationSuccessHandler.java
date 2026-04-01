package com.apps.quantitymeasurement.security;

import com.apps.quantitymeasurement.config.GoogleOAuth2Properties;
import com.apps.quantitymeasurement.model.AppUserEntity;
import com.apps.quantitymeasurement.service.AppUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final GoogleOAuth2Properties googleOAuth2Properties;
    private final AppUserService appUserService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            throw new ServletException("Google account did not return an email address");
        }

        AppUserEntity user = appUserService.upsertGoogleUser(email, name);
        List<String> roles = user.getRoles().stream().toList();

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("authProvider", "GOOGLE");
        claims.put("name", user.getFullName());
        claims.put("email", user.getEmail());

        String token = jwtService.generateToken(user.getUsername(), roles, claims);

        String redirectUrl = UriComponentsBuilder.fromUriString(googleOAuth2Properties.getAuthorizedRedirectUri())
                .queryParam("token", token)
                .queryParam("email", user.getEmail())
                .queryParam("username", user.getUsername())
                .queryParam("provider", "GOOGLE")
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
