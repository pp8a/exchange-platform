package com.fintech.gateway.auth.service;

import com.fintech.gateway.auth.dto.AuthRequest;
import com.fintech.gateway.auth.dto.AuthResponse;
import com.fintech.gateway.auth.dto.UserDTO;
import com.fintech.gateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;
    
    public Mono<AuthResponse> authenticate(AuthRequest request) {
    	boolean isEmail = request.identifier().contains("@");
    	log.info("🔐 [AUTH] Request received for identifier: {}", request.identifier());
    	
    	 Mono<UUID> userIdMono = isEmail
                 ? getUserIdByEmail(request.identifier())
                 : getUserIdByPhone(request.identifier());
    	
    	 return userIdMono
                 .flatMap(userId -> getHashedPassword(userId)                		 
                         .flatMap(hashed -> {
                        	 if (!BCrypt.checkpw(request.password(), hashed)) {
                                 return Mono.error(new RuntimeException("Invalid password"));
                             }                      	
                        	    
                             String token = jwtUtil.generateToken(userId);
                             return Mono.just(new AuthResponse(token));
                         }));    	
    }
    
    private Mono<UUID> getUserIdByEmail(String email) {
    	log.info("🌐 WebClient: GET userId by email: {}", email);
        return webClientBuilder.build()
                .get()
                .uri("http://currency-service/emails/user-id?email={email}", email)
                .retrieve()
                .bodyToMono(UUID.class);
    }

    private Mono<UUID> getUserIdByPhone(String phone) {
    	log.info("🌐 WebClient: GET userId by phone: {}", phone);
        return webClientBuilder.build()
                .get()
                .uri("http://currency-service/phones/user-id?phone={phone}", phone)
                .retrieve()
                .bodyToMono(UUID.class);
    }
    
    private Mono<String> getHashedPassword(UUID userId) {
    	log.info("🌐 WebClient: GET user by id to fetch password: {}", userId);
        return webClientBuilder.build()
                .get()
                .uri("http://currency-service/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .map(UserDTO::password); // предполагается, что password есть в UserDTO
    }
}
