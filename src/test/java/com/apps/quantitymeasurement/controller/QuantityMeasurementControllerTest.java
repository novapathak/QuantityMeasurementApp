package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.config.SecurityConfig;
import com.apps.quantitymeasurement.exception.GlobalExceptionHandler;
import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityInputDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuantityMeasurementController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IQuantityMeasurementService service;

    @Test
    void testCompareQuantities() throws Exception {
        when(service.compare(any(), any())).thenReturn(compareResponse());

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lengthRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operation").value("COMPARE"))
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void testAddQuantities() throws Exception {
        when(service.add(any(), any(), eq("FEET"))).thenReturn(addResponse());

        mockMvc.perform(post("/api/v1/quantities/add")
                        .queryParam("targetUnit", "FEET")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lengthRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("ADD"))
                .andExpect(jsonPath("$.resultValue").value(2.0));
    }

    @Test
    void testConvertQuantities() throws Exception {
        when(service.convert(any(), any(), eq("INCHES"))).thenReturn(convertResponse());

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .queryParam("targetUnit", "INCHES")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new QuantityInputDTO(
                                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                                null
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("CONVERT"))
                .andExpect(jsonPath("$.resultValue").value(12.0));
    }

    @Test
    void testGetHistoryByOperation() throws Exception {
        when(service.getHistoryByOperation("COMPARE")).thenReturn(List.of(compareResponse()));

        mockMvc.perform(get("/api/v1/quantities/history/operation/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("COMPARE"));
    }

    @Test
    void testGetHistoryByMeasurementType() throws Exception {
        when(service.getHistoryByMeasurementType("LengthUnit")).thenReturn(List.of(addResponse()));

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].thisMeasurementType").value("LengthUnit"));
    }

    @Test
    void testGetCountByOperation() throws Exception {
        when(service.countByOperation("COMPARE")).thenReturn(3L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void testInvalidInputReturnsBadRequest() throws Exception {
        QuantityInputDTO invalidRequest = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHE", "LengthUnit")
        );

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"))
                .andExpect(jsonPath("$.message").value("Unit must be valid for the specified measurement type"));
    }

    @Test
    void testContentNegotiationXml() throws Exception {
        when(service.compare(any(), any())).thenReturn(compareResponse());

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_XML)
                        .content(objectMapper.writeValueAsString(lengthRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML));
    }

    private QuantityInputDTO lengthRequest() {
        return new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit")
        );
    }

    private QuantityMeasurementDTO compareResponse() {
        return new QuantityMeasurementDTO(
                1.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit",
                "COMPARE",
                0.0, null, null,
                "true",
                null, false
        );
    }

    private QuantityMeasurementDTO addResponse() {
        return new QuantityMeasurementDTO(
                1.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit",
                "ADD",
                2.0, "FEET", "LengthUnit",
                null,
                null, false
        );
    }

    private QuantityMeasurementDTO convertResponse() {
        return new QuantityMeasurementDTO(
                1.0, "FEET", "LengthUnit",
                0.0, "INCH", "LengthUnit",
                "CONVERT",
                12.0, "INCH", "LengthUnit",
                null,
                null, false
        );
    }
}
