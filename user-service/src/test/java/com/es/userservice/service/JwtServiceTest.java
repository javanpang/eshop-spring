package com.es.userservice.service;

import com.es.userservice.model.User;
import com.es.userservice.security.JwtService;
import com.es.userservice.util.UserTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private static final String TEST_SECRET = "sv5tXPPiATSXpSAPN3fRMBDkjqnIUfy36PUh1Zmluu5";
    private static final long TEST_EXPIRATION = 86400000;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", TEST_EXPIRATION);
    }

    @Test
    void generateToken_returnsNonNullToken() {
        User user = UserTestDataBuilder.buildUser();

        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void extractEmail_returnsCorrectEmail() {
        User user = UserTestDataBuilder.buildUser();
        String token = jwtService.generateToken(user);

        String extractedEmail = jwtService.extractEmail(token);

        assertThat(extractedEmail).isEqualTo(UserTestDataBuilder.DEFAULT_EMAIL);
    }

    @Test
    void isTokenValid_withValidToken_returnsTrue() {
        User user = UserTestDataBuilder.buildUser();
        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertThat(isValid).isTrue();
    }

    @Test
    void isTokenValid_withWrongUser_returnsFalse() {
        User user = UserTestDataBuilder.buildUser();
        String token = jwtService.generateToken(user);

        User differentUser = UserTestDataBuilder.buildUser();
        differentUser.setEmail("different@example.com");

        boolean isValid = jwtService.isTokenValid(token, differentUser);

        assertThat(isValid).isFalse();
    }

    @Test
    void isTokenValid_withExpiredToken_returnsFalse() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1);

        User user = UserTestDataBuilder.buildUser();
        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertThat(isValid).isFalse();
    }
}
