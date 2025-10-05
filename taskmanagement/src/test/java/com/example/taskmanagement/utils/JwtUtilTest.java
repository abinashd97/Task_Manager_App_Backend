package com.example.taskmanagement.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateAndExtractUsername() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void testValidateToken_ValidToken() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    void testValidateToken_DifferentUsername() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtUtil.generateToken("otheruser");

        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    void testValidateToken_ExpiredToken() throws Exception {
        // Use reflection to access the SECRET_KEY field
        Field field = JwtUtil.class.getDeclaredField("SECRET_KEY");
        field.setAccessible(true);
        SecretKey secretKey = (SecretKey) field.get(null);

        // Create an already expired token
        String expiredToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // expired 1 sec ago
                .signWith(secretKey)
                .compact();

        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        boolean isValid = false;
        try {
        	isValid = jwtUtil.validateToken(expiredToken, userDetails);
        	
        }catch(Exception e) {
        	System.out.println("Exception occured: "+e.getMessage());
        }
        assertThat(isValid).isFalse();

        

        
    }
}
