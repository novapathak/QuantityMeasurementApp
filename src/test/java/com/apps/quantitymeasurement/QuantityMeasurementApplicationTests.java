package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.model.AuthRequest;
import com.apps.quantitymeasurement.model.AuthResponse;
import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityInputDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;
import com.apps.quantitymeasurement.model.RegisterRequest;
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

import java.util.List;

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
    void testLoginEndpointReturnsJwt() {
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                authBaseUrl() + "/login",
                new HttpEntity<>(new AuthRequest("admin", "Admin@12345"), jsonHeaders()),
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAccessToken());
        assertEquals("LOCAL", response.getBody().getAuthProvider());
    }

    @Test
    void testRegisterEndpointReturnsJwt() {
        String username = "user" + System.nanoTime();
        String email = username + "@example.com";

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                authBaseUrl() + "/register",
                new HttpEntity<>(new RegisterRequest(username, email, "Registered User", "StrongPass123"), jsonHeaders()),
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(username, response.getBody().getUsername());
        assertEquals(email, response.getBody().getEmail());
        assertNotNull(response.getBody().getAccessToken());
    }

    @Test
    void testUnauthorizedWithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                authBaseUrl() + "/me",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testRestEndpointCompareQuantitiesWithJwt() {
        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
                quantityBaseUrl() + "/compare",
                HttpMethod.POST,
                new HttpEntity<>(lengthRequest(), authHeaders()),
                QuantityMeasurementDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("COMPARE", response.getBody().getOperation());
        assertEquals("true", response.getBody().getResultString());
    }

    @Test
    void testRestEndpointConvertQuantitiesWithJwt() {
        QuantityInputDTO convertRequest = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                null
        );

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
                quantityBaseUrl() + "/convert?targetUnit=INCHES",
                HttpMethod.POST,
                new HttpEntity<>(convertRequest, authHeaders()),
                QuantityMeasurementDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CONVERT", response.getBody().getOperation());
        assertEquals(12.0, response.getBody().getResultValue(), 0.01);
    }

    @Test
    void testRestEndpointAddQuantitiesWithJwt() {
        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
                quantityBaseUrl() + "/add",
                HttpMethod.POST,
                new HttpEntity<>(lengthRequest(), authHeaders()),
                QuantityMeasurementDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADD", response.getBody().getOperation());
        assertEquals(2.0, response.getBody().getResultValue(), 0.01);
    }

    @Test
    void testRestEndpointInvalidInputReturns400WithJwt() {
        QuantityInputDTO invalidRequest = new QuantityInputDTO(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(12.0, "INCHE", "LengthUnit")
        );

        ResponseEntity<String> response = restTemplate.exchange(
                quantityBaseUrl() + "/compare",
                HttpMethod.POST,
                new HttpEntity<>(invalidRequest, authHeaders()),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAuthenticatedProfileEndpoint() {
        ResponseEntity<AuthResponse> response = restTemplate.exchange(
                authBaseUrl() + "/me",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin", response.getBody().getUsername());
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
        assertTrue(response.getBody().contains("\"bearerAuth\""));
        assertTrue(response.getBody().contains("\"scheme\":\"bearer\""));
        assertTrue(response.getBody().contains("\"bearerFormat\":\"JWT\""));
        assertTrue(response.getBody().contains("\"/api/v1/quantities/add\""));
        assertTrue(response.getBody().contains("\"security\":[{\"bearerAuth\":[]}]"));
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
    void testContentNegotiationXmlWithJwt() {
        HttpHeaders headers = authHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));

        ResponseEntity<String> response = restTemplate.exchange(
                quantityBaseUrl() + "/compare",
                HttpMethod.POST,
                new HttpEntity<>(lengthRequest(), headers),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getContentType() != null);
        assertTrue(MediaType.APPLICATION_XML.isCompatibleWith(response.getHeaders().getContentType()));
    }

    private String quantityBaseUrl() {
        return "http://localhost:" + port + "/api/v1/quantities";
    }

    private String authBaseUrl() {
        return "http://localhost:" + port + "/api/v1/auth";
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

    private HttpHeaders authHeaders() {
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                authBaseUrl() + "/login",
                new HttpEntity<>(new AuthRequest("admin", "Admin@12345"), jsonHeaders()),
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertNotNull(loginResponse.getBody().getAccessToken());

        HttpHeaders headers = jsonHeaders();
        headers.setBearerAuth(loginResponse.getBody().getAccessToken());
        return headers;
    }
}
