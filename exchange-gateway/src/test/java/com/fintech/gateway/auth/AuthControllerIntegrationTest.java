package com.fintech.gateway.auth;

import com.fintech.gateway.auth.config.TestContainersWebClientConfig;
import com.fintech.gateway.auth.dto.AuthRequest;
import com.fintech.gateway.auth.dto.AuthResponse;
import com.fintech.gateway.security.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("testcontainers")
@Import(TestContainersWebClientConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIntegrationTest {
	//TODO: если вдруг currency-service не поднят в Docker то убрать комментарии для теста
//	static final GenericContainer<?> currencyService =
//            new GenericContainer<>(DockerImageName.parse("your/currency-service:latest"))
//                    .withExposedPorts(8081);
	
	@Autowired
    WebTestClient webTestClient;

    @Autowired
    JwtUtil jwtUtil;

    UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    String email = "alice@example.com";
    String password = "password1";

//    @BeforeAll
//    static void startCurrencyService() {
//        currencyService.start();
//    }
//
//    @AfterAll
//    static void stopCurrencyService() {
//        currencyService.stop();
//    }
    
    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
//        String currencyBaseUrl = "http://" + currencyService.getHost() + ":" + currencyService.getFirstMappedPort();
//        registry.add("currency.service.base-url", () -> currencyBaseUrl); // используется в WebClientConfig
    	registry.add("currency.service.base-url", () -> "http://localhost:8081");
    }
    
    @Test
    @DisplayName("✅ Should authenticate successfully with real container")
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
}
