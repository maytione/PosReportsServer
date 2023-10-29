package com.em.reportserver.config;

import com.em.reportserver.user.Role;
import com.em.reportserver.user.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(value = "test")
class JwtServiceTest {

    private static String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5ODQ5OTUyMiwiZXhwIjoxNjk4NTAwOTYyfQ.FZdV8PYJf8idvUIWLi53Yu8u9jBPC79NJu49m3k4E4w";

    @Autowired
    private JwtService underTest;


    @Test
    @Order(1)
    void generateToken() {
        User admin = User.builder()
                .id(99L)
                .email("jwt@program.hr")
                .firstname("Json")
                .lastname("Web Token")
                .username("jwt")
                .role(Role.ADMIN)
                .build();

        TOKEN = underTest.generateToken(admin);
        assertThat(TOKEN).isNotEmpty();
        assertThat(TOKEN).hasSizeBetween(1,256);
    }


    @Test
    @Order(2)
    void extractUsername() {

        String username = underTest.extractUsername(TOKEN);
        assertEquals("jwt", username);
    }

    @Test
    @Order(3)
    void extractClaim() {
        String subject = underTest.extractClaim(TOKEN, Claims::getSubject);
        underTest.extractClaim(TOKEN, Claims::getExpiration);
        assertEquals("jwt", subject);
    }

    @Test
    @Order(5)
    void isTokenValid() {
        User admin = User.builder()
                .id(99L)
                .email("jwt@program.hr")
                .firstname("Json")
                .lastname("Web Token")
                .username("jwt")
                .role(Role.ADMIN)
                .build();
        assertTrue(underTest.isTokenValid(TOKEN, admin));
    }


}