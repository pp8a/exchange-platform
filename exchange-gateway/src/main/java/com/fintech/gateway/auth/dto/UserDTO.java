package com.fintech.gateway.auth.dto;

import java.util.UUID;

public record UserDTO(UUID id, String name, String password) {}
