package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityInputDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void testSpringBootApplicationStarts() {
        assertTrue(port > 0);
    }

    @Test
    void testRestEndpointCompareQuantities() {
        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare",
                new HttpEntity<>(lengthRequest(), jsonHeaders()),
                QuantityMeasurementDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("COMPARE", response.getBody().getOperation());
        assertEquals("true", response.getBody().getResultString());
    }

    @Test
    void testRestEndpointConvertQuantities() {
        QuantityInputDTO convertRequest = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                null
        );

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/convert?targetUnit=INCHES",
                new HttpEntity<>(convertRequest, jsonHeaders()),
                QuantityMeasurementDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CONVERT", response.getBody().getOperation());
        assertEquals(12.0, response.getBody().getResultValue(), 0.01);
    }

    @Test
    void testRestEndpointAddQuantities() {
        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/add",
                new HttpEntity<>(lengthRequest(), jsonHeaders()),
                QuantityMeasurementDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADD", response.getBody().getOperation());
        assertEquals(2.0, response.getBody().getResultValue(), 0.01);
    }

    @Test
    void testRestEndpointInvalidInputReturns400() {
        QuantityInputDTO invalidRequest = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHE", "LengthUnit")
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl() + "/compare",
                new HttpEntity<>(invalidRequest, jsonHeaders()),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSwaggerUiLoads() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/swagger-ui.html", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("Swagger UI"));
    }

    @Test
    void testOpenApiDocumentationLoads() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api-docs", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("\"openapi\""));
    }

    @Test
    void testH2ConsoleLaunches() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/h2-console", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("H2 Console"));
    }

    @Test
    void testActuatorHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/actuator/health", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("\"status\":\"UP\""));
    }

    @Test
    void testActuatorMetricsEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/actuator/metrics", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("\"names\""));
    }

    @Test
    void testContentNegotiationXml() {
        HttpHeaders headers = jsonHeaders();
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_XML));

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/compare",
                HttpMethod.POST,
                new HttpEntity<>(lengthRequest(), headers),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getContentType() != null);
        assertTrue(MediaType.APPLICATION_XML.isCompatibleWith(response.getHeaders().getContentType()));
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api/v1/quantities";
    }

    private QuantityInputDTO lengthRequest() {
        return new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHES", "LengthUnit")
        );
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
