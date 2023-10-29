package com.em.reportserver.user;

import com.em.reportserver.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(value = "test")
class UserRepositoryTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldFindByUsername() {

        var admin = UserDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@program.hr")
                .username("john")
                .password("secret")
                .role(Role.USER)
                .build();

        authenticationService.register(admin);

        Optional<User> result = underTest.findByUsername(admin.getUsername());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isNotNull();
        assertEquals(result.get().getUsername(),admin.getUsername());
    }

}