package com.em.reportserver.auth;

import com.em.reportserver.user.Role;
import com.em.reportserver.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles(value = "test")
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService underTest;


    @Test
    void itShouldRegister() {

        var user = UserDto.builder()
                .firstname("John")
                .lastname("Smith")
                .email("john.smith@program.hr")
                .username("smithy")
                .password("my_secret")
                .role(Role.USER)
                .build();

        AuthenticationResponse response = underTest.register(user);
        assertThat(response).isInstanceOf(AuthenticationResponse.class);
        assertThat(response.getToken()).isNotEmpty();

    }


    @Test
    void itShouldAuthenticate() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username("smithy")
                .password("my_secret")
                .build();
        AuthenticationResponse response = underTest.authenticate(authenticationRequest);
        assertThat(response.getToken()).isNotEmpty();
    }

    @Test
    void itShouldThrowBadCredentialsAuthenticate() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username("admin")
                .password("wrong")
                .build();
        assertThatThrownBy(() -> underTest.authenticate(authenticationRequest))
                .hasMessage("Bad credentials");
    }
}