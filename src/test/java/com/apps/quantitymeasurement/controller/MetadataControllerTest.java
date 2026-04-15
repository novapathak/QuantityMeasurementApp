package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.config.GoogleOAuth2Properties;
import com.apps.quantitymeasurement.security.JwtService;
import com.apps.quantitymeasurement.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetadataController.class)
@AutoConfigureMockMvc(addFilters = false)
class MetadataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleOAuth2Properties googleOAuth2Properties;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AppUserService appUserService;

    @Test
    void testMeasurementMetadataEndpoint() throws Exception {
        when(googleOAuth2Properties.isEnabled()).thenReturn(false);

        mockMvc.perform(get("/api/v1/metadata/measurements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.googleOAuthEnabled").value(false))
                .andExpect(jsonPath("$.measurementTypes[0].name").value("LengthUnit"))
                .andExpect(jsonPath("$.measurementTypes[0].units[0]").value("INCH"))
                .andExpect(jsonPath("$.operations[0].name").value("ADD"))
                .andExpect(jsonPath("$.operations[5].requiresTargetUnit").value(true));
    }
}
