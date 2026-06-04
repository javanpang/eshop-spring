package com.es.userservice.service;

import com.es.userservice.dto.UserResponseDTO;
import com.es.userservice.exception.UserNotFoundException;
import com.es.userservice.mapper.UserMapper;
import com.es.userservice.model.User;
import com.es.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toDTO(user);
    }
}
