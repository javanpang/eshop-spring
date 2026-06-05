package com.es.userservice.service;

import com.es.userservice.dto.AuthResponseDTO;
import com.es.userservice.dto.LoginRequestDTO;
import com.es.userservice.dto.RegisterRequestDTO;
import com.es.userservice.exception.EmailAlreadyExistsException;
import com.es.userservice.model.User;
import com.es.userservice.repository.UserRepository;
import com.es.userservice.util.UserTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    // --- register ---

    @Test
    void register_withValidRequest_savesUserAndReturnsToken() {
        RegisterRequestDTO request = UserTestDataBuilder.buildRegisterRequestDTO();
        User savedUser = UserTestDataBuilder.buildUser();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("mocked.jwt.token");

        AuthResponseDTO result = authService.register(request);

        assertThat(result.getToken()).isEqualTo("mocked.jwt.token");
        assertThat(result.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(result.getFirstName()).isEqualTo(savedUser.getFirstName());
        assertThat(result.getLastName()).isEqualTo(savedUser.getLastName());
        assertThat(result.getRole()).isEqualTo(savedUser.getRole().name());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(request.getPassword());
    }

    @Test
    void register_withExistingEmail_throwsEmailAlreadyInUseException() {
        RegisterRequestDTO request = UserTestDataBuilder.buildRegisterRequestDTO();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request)).isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(request.getEmail());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_passwordIsHashed_notStoredAsPlainText() {
        RegisterRequestDTO request = UserTestDataBuilder.buildRegisterRequestDTO();
        User savedUser = UserTestDataBuilder.buildUser();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("token");

        authService.register(request);

        verify(passwordEncoder).encode(UserTestDataBuilder.DEFAULT_PASSWORD);
        verify(userRepository).save(argThat(user -> !user.getPassword().equals(UserTestDataBuilder.DEFAULT_PASSWORD)));
    }

    // --- login ---

    @Test
    void login_withValidCredentials_returnsToken() {
        LoginRequestDTO request = UserTestDataBuilder.buildLoginRequestDTO();
        User user = UserTestDataBuilder.buildUser();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mocked.jwt.token");

        AuthResponseDTO result = authService.login(request);

        assertThat(result.getToken()).isEqualTo("mocked.jwt.token");
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_withInvalidCredentials_throwsBadCredentialsException() {
        LoginRequestDTO request = UserTestDataBuilder.buildLoginRequestDTO();

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(BadCredentialsException.class);

        verify(userRepository, never()).findByEmail(any());
    }
}
