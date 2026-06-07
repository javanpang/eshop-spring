package com.es.productservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class JwtTestHelper {
    private static final String TEST_SECRET = "52e15ba064e34e75f7f72d1d972d39adb9f863152bf66dc6db1574abfc33d169";

    private JwtTestHelper() {
    }

    public static String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET)))
                .compact();
    }

    public static String generateAdminToken() {
        return generateToken("admin@example.com", "ADMIN");
    }

    public static String generateUserToken() {
        return generateToken("user@example.com", "USER");
    }
}
