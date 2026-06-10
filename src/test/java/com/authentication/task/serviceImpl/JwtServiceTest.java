package com.authentication.task.serviceImpl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.authentication.task.security.JwtService;

/**
 * Unit tests for {@link JwtService}.
 * Uses ReflectionTestUtils to inject @Value fields without Spring context.
 */
class JwtServiceTest {

    private JwtService jwtService;

    private static final String TEST_SECRET =
            "CfxSN3ALQ6ORE0Wfub4w75a4lDUE5MzXqaXdE5wZAC8=";
    private static final long   EXPIRATION_MS = 3_600_000L; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",       TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", EXPIRATION_MS);
    }

    // ── generateToken ───────────────────────────────────────────────────────

    @Test
    @DisplayName("generateToken() – produces a non-blank token")
    void generateToken_shouldReturnNonBlankToken() {
        String token = jwtService.generateToken("testuser");

        assertThat(token).isNotBlank();
    }

    // ── extractUsername ─────────────────────────────────────────────────────

    @Test
    @DisplayName("extractUsername() – returns the username embedded in the token")
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateToken("testuser");

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("testuser");
    }

    // ── validateToken ───────────────────────────────────────────────────────

    @Test
    @DisplayName("validateToken() – returns true for valid token and matching user")
    void validateToken_shouldReturnTrue_forValidToken() {
        String token = jwtService.generateToken("testuser");

        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("validateToken() – returns false when username does not match")
    void validateToken_shouldReturnFalse_forWrongUser() {
        String token = jwtService.generateToken("testuser");

        UserDetails otherUser = User.builder()
                .username("anotheruser")
                .password("password")
                .roles("USER")
                .build();

        boolean isValid = jwtService.validateToken(token, otherUser);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("validateToken() – returns false for an expired token")
    void validateToken_shouldReturnFalse_forExpiredToken() {
        // Override expiration to 1 ms so the token is immediately expired
        ReflectionTestUtils.setField(jwtService, "expirationMs", 1L);
        String token = jwtService.generateToken("testuser");

        // Wait just a bit to ensure expiry
        try { Thread.sleep(5); } catch (InterruptedException ignored) {}

        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        // JJWT throws ExpiredJwtException – service should propagate it
        assertThatThrownBy(() -> jwtService.validateToken(token, userDetails))
                .isInstanceOf(Exception.class);
    }
}
