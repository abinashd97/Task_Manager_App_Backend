package com.example.taskmanagement.utils;


import com.example.taskmanagement.serviceimpl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtFilter;

    @Test
    void passwordEncoderBean_ShouldEncodePasswords() {
        String rawPassword = "mySecret";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void authenticationManagerBean_ShouldBeCreated() {
        assertThat(authenticationManager).isNotNull();
    }

    @Test
    void securityConfigBean_ShouldLoad() {
        assertThat(securityConfig).isNotNull();
    }

    @Test
    void givenPublicEndpoint_register_ShouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/auth/register"))
                .andExpect(status().isOk()); // expecting 200 OK for public endpoint
    }
}
