package com.es.userservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDTO {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
