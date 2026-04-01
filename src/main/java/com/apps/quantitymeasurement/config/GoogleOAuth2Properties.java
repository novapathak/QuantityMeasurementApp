package com.apps.quantitymeasurement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.oauth2.google")
public class GoogleOAuth2Properties {

    private boolean enabled = false;
    private String clientId;
    private String clientSecret;
    private String redirectUri = "{baseUrl}/login/oauth2/code/{registrationId}";
    private String authorizedRedirectUri = "http://localhost:3000/oauth2/redirect";
    private List<String> scopes = new ArrayList<>(List.of("openid", "email", "profile"));
}
