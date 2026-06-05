package com.es.userservice.integration;

import com.es.userservice.dto.AuthResponseDTO;
import com.es.userservice.dto.LoginRequestDTO;
import com.es.userservice.dto.RegisterRequestDTO;
import com.es.userservice.repository.UserRepository;
import com.es.userservice.service.JwtService;
import com.es.userservice.util.UserTestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    private String registerAndGetToken() throws Exception {
        RegisterRequestDTO request = UserTestDataBuilder.buildRegisterRequestDTO();
        MvcResult result = mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponseDTO.class).getToken();
    }

    // --- register ---

    @Test
    void register_withValidRequest_returns201AndToken() throws Exception {
        RegisterRequestDTO request = UserTestDataBuilder.buildRegisterRequestDTO();

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.firstName").value(request.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(request.getLastName()))
                .andExpect(jsonPath("$.role").value("USER"));

        assertThat(userRepository.findByEmail(request.getEmail())).isPresent();
    }

    @Test
    void register_passwordIsHashed_notStoredAsPlainText() throws Exception {
        RegisterRequestDTO request = UserTestDataBuilder.buildRegisterRequestDTO();

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());

        String storedPassword = userRepository.findByEmail(request.getEmail()).orElseThrow().getPassword();

        assertThat(storedPassword).isNotEqualTo(UserTestDataBuilder.DEFAULT_PASSWORD);
        assertThat(passwordEncoder.matches(UserTestDataBuilder.DEFAULT_PASSWORD, storedPassword)).isTrue();
    }

    @Test
    void register_withDuplicateEmail_returns409() throws Exception {
        RegisterRequestDTO request = UserTestDataBuilder.buildRegisterRequestDTO();

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    void register_withInvalidRequest_returns400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserTestDataBuilder.buildInvalidRegisterRequestDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    // --- login ---

    @Test
    void login_withValidCredentials_returns200AndToken() throws Exception {
        registerAndGetToken();

        LoginRequestDTO request = UserTestDataBuilder.buildLoginRequestDTO();

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    void login_withInvalidCredentials_returns401() throws Exception {
        registerAndGetToken();

        LoginRequestDTO badLoginRequest = new LoginRequestDTO();
        badLoginRequest.setEmail(UserTestDataBuilder.DEFAULT_EMAIL);
        badLoginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void login_withUnknownEmail_returns401() throws Exception {
        LoginRequestDTO unknownUserRequest = new LoginRequestDTO();
        unknownUserRequest.setEmail("unknown@example.com");
        unknownUserRequest.setPassword("password123");

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unknownUserRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    // --- GET /users/me (protected) ---

    @Test
    void getMe_withValidToken_returns200AndUser() throws Exception {
        String token = registerAndGetToken();

        mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(UserTestDataBuilder.DEFAULT_EMAIL))
                .andExpect(jsonPath("$.firstName").value(UserTestDataBuilder.DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(UserTestDataBuilder.DEFAULT_LAST_NAME))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getMe_withNoToken_returns401() throws Exception {
        mockMvc.perform(post("/users/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void getMe_withMalformedToken_returns401() throws Exception {
        mockMvc.perform(get("/users/me").header("Authorization", "Bearer this.is.not.a.valid.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMe_withExpiredToken_returns401() throws Exception {
        org.springframework.test.util.ReflectionTestUtils.setField(jwtService, "expiration", -1);

        String expiredToken = jwtService.generateToken(UserTestDataBuilder.buildUser());

        org.springframework.test.util.ReflectionTestUtils.setField(jwtService, "expiration", 8640000);

        mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }
}
