package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuantityMeasurementControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/v1/quantities";
    }

    @Test
    void testCompareQuantities() {

        QuantityMeasurementDTO request = new QuantityMeasurementDTO(
                1.0, "FEET", "LENGTH",
                12.0, "INCH", "LENGTH",
                "COMPARE",
                null, null, null,
                null, null, false
        );

        ResponseEntity<QuantityMeasurementDTO> response =
                restTemplate.postForEntity(baseUrl() + "/compare", request, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("COMPARE", response.getBody().getOperation());
    }

    @Test
    void testAddQuantities() {

        QuantityMeasurementDTO request = new QuantityMeasurementDTO(
                1.0, "FEET", "LENGTH",
                12.0, "INCH", "LENGTH",
                "ADD",
                null, null, null,
                null, null, false
        );

        ResponseEntity<QuantityMeasurementDTO> response =
                restTemplate.postForEntity(baseUrl() + "/add", request, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADD", response.getBody().getOperation());
    }

    @Test
    void testConvertQuantities() {

        QuantityMeasurementDTO request = new QuantityMeasurementDTO(
                1.0, "FEET", "LENGTH",
                null, null, null,
                "CONVERT",
                null, null, null,
                null, null, false
        );

        ResponseEntity<QuantityMeasurementDTO> response =
                restTemplate.postForEntity(baseUrl() + "/convert?targetUnit=INCH", request, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetHistory() {

        ResponseEntity<QuantityMeasurementDTO[]> response =
                restTemplate.getForEntity(baseUrl() + "/history", QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetCountByOperation() {

        ResponseEntity<Long> response =
                restTemplate.getForEntity(baseUrl() + "/count/COMPARE", Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testInvalidInput_ReturnsBadRequest() {

        String invalidJson = "{ invalid json }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(invalidJson, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/compare", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}