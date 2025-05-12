package com.fintech.gateway.auth;

import com.fintech.gateway.auth.config.TestWebClientConfig;
import com.fintech.gateway.auth.dto.AuthRequest;
import com.fintech.gateway.auth.dto.AuthResponse;
import com.fintech.gateway.security.JwtUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Import(TestWebClientConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerWebTest {
	
	private static WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtUtil jwtUtil;

    private final UUID userId = UUID.randomUUID();
    private final String email = "test@example.com";
    private final String password = "password123";
    private final String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    
    @BeforeAll
    static void startWireMock() {
    	wireMockServer = new WireMockServer(
    	        WireMockConfiguration.options()
    	            .port(8089)
    	            .disableRequestJournal() // важно для Jakarta/без servlet
    	            .asynchronousResponseEnabled(true)
    	    );
        wireMockServer.start();
        configureFor("localhost", 8089);
    }
    
    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }
    
    @BeforeEach
    void setupMocks() {
        wireMockServer.resetAll();

        // mock: GET /emails/user-id
        stubFor(get(urlPathEqualTo("/emails/user-id"))
                .withQueryParam("email", equalTo(email))
                .willReturn(okJson("\"" + userId.toString() + "\"")));

        // mock: GET /users/{id}
        stubFor(get(urlPathEqualTo("/users/" + userId))
                .willReturn(okJson("{ \"password\": \"" + passwordHash + "\" }")));
    }
    
    @Test
    @DisplayName("✅ Should authenticate successfully and return token")
    void shouldAuthenticateSuccessfully() {
        AuthRequest request = new AuthRequest(email, password);

        webTestClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponse.class)
                .value(response -> {
                	Assertions.assertNotNull(response.accessToken());
                    UUID extracted = jwtUtil.validateTokenAndGetUserId(response.accessToken());
                    Assertions.assertEquals(userId, extracted);
                });
    }
    
    @Test
    @DisplayName("❌ Should fail with invalid password")
    void shouldFailInvalidPassword() {
        AuthRequest request = new AuthRequest(email, "wrongPassword");

        webTestClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();
    }
    
}
