package com.em.reportserver.config;


import com.em.reportserver.user.Role;
import com.em.reportserver.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles(value = "test")
class JwtAuthenticationFilterTest {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter underTest;

    @BeforeEach
    void setUp() {
        underTest = new JwtAuthenticationFilter(jwtService,userDetailsService);
    }
    @Test
    void doFilterInternal() throws ServletException, IOException {

        User admin = User.builder()
                .id(99L)
                .email("admin@program.hr")
                .firstname("Admin")
                .lastname("Admin")
                .username("admin")
                .password("password")
                .role(Role.ADMIN)
                .build();

        String jwt = jwtService.generateToken(admin);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        underTest.doFilterInternal(request,response,filterChain);
    }
}