package com.es.userservice.util;

import com.es.userservice.dto.LoginRequestDTO;
import com.es.userservice.dto.RegisterRequestDTO;
import com.es.userservice.model.Role;
import com.es.userservice.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserTestDataBuilder {

    public static final UUID DEFAULT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final String DEFAULT_EMAIL = "john.doe@example.com";
    public static final String DEFAULT_PASSWORD = "password123";
    public static final String DEFAULT_HASHED_PASSWORD = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
    public static final String DEFAULT_FIRST_NAME = "John";
    public static final String DEFAULT_LAST_NAME = "Doe";
    public static final Role DEFAULT_ROLE = Role.USER;

    private UserTestDataBuilder() {
    }

    public static User buildUser() {
        return User.builder()
                .id(DEFAULT_ID)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_HASHED_PASSWORD)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .role(DEFAULT_ROLE)
                .createdAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();
    }

    public static RegisterRequestDTO buildRegisterRequestDTO() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail(DEFAULT_EMAIL);
        dto.setPassword(DEFAULT_PASSWORD);
        dto.setFirstName(DEFAULT_FIRST_NAME);
        dto.setLastName(DEFAULT_LAST_NAME);
        return dto;
    }

    public static RegisterRequestDTO buildInvalidRegisterRequestDTO() {
        return new RegisterRequestDTO();
    }

    public static LoginRequestDTO buildLoginRequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail(DEFAULT_EMAIL);
        dto.setPassword(DEFAULT_PASSWORD);
        return dto;
    }
}
