package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.oauth2.google.enabled=true",
        "app.oauth2.google.client-id=test-google-client-id",
        "app.oauth2.google.client-secret=test-google-client-secret",
        "app.oauth2.google.authorized-redirect-uri=http://localhost:3000/oauth2/redirect"
})
@AutoConfigureMockMvc
class OAuth2AuthorizationFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGoogleAuthorizationRedirect() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("accounts.google.com")));
    }
}
