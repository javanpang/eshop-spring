package com.es.userservice.mapper;

import com.es.userservice.dto.UserResponseDTO;
import com.es.userservice.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
