package com.em.reportserver.user;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(value = "test")
class UserServiceImplTest {


    private UserServiceImpl underTest;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(passwordEncoder, userRepository);
    }


    @Test
    @Order(1)
    void createUser() {
        User user = User.builder()
                .username("jane")
                .email("jane@program.hr")
                .firstname("Jane")
                .lastname("Smith")
                .password("secret")
                .role(Role.USER)
                .build();

        Optional<User> userOptional = Optional.of(user);
        given(userRepository.save(user)).willReturn(userOptional.get());

        User jane = underTest.createUser(user);
        assertNotNull(jane);
        assertEquals(jane.getUsername(), user.getUsername());
        assertEquals(jane.getRole(), user.getRole());
        assertEquals(jane.getEmail(), user.getEmail());
        assertEquals(jane.getLastname(), user.getLastname());
        assertEquals(jane.getFirstname(), user.getFirstname());
        assertEquals(jane.getPassword(), user.getPassword());

    }

    @Test
    @Order(2)
    void getAllUsers() {
        User user = User.builder()
                .username("jane")
                .email("jane@program.hr")
                .firstname("Jane")
                .lastname("Smith")
                .password("secret")
                .role(Role.USER)
                .build();

        List<User> userList = new ArrayList<>();
        userList.add(user);

        given(userRepository.findAll()).willReturn(userList);

        List<User> allUsers = underTest.getAllUsers();
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
    }

    @Test
    @Order(3)
    void updateUser() {
        User user = User.builder()
                .id(1L)
                .username("jane")
                .email("jane@program.hr")
                .firstname("Jane")
                .lastname("Smith")
                .password("secret")
                .role(Role.USER)
                .build();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        underTest.updateUser(user.getId(), user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();


        assertNotNull(user);
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.getRole(), capturedUser.getRole());
        assertEquals(user.getEmail(), capturedUser.getEmail());
        assertEquals(user.getLastname(), capturedUser.getLastname());
        assertEquals(user.getFirstname(), capturedUser.getFirstname());
        assertTrue(capturedUser.getPassword().startsWith("$"));


    }

    @Test
    @Order(4)
    void getUserById() {
        User user = User.builder()
                .id(1L)
                .username("jane")
                .email("jane@program.hr")
                .firstname("Jane")
                .lastname("Smith")
                .password("secret")
                .role(Role.USER)
                .build();


        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        User userById = underTest.getUserById(user.getId());
        assertNotNull(userById);
    }

    @Test
    @Order(4)
    void getUserByIdNotFoundException() {
        long userid = 99;
        assertThatThrownBy(() -> underTest.getUserById(userid))
                .hasMessage("Get - User not found with id " + userid);
    }

    @Test
    @Order(5)
    void getUserByUsername() {
        User user = User.builder()
                .id(1L)
                .username("jane")
                .email("jane@program.hr")
                .firstname("Jane")
                .lastname("Smith")
                .password("secret")
                .role(Role.USER)
                .build();


        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        User userByUsername = underTest.getUserByUsername(user.getUsername());
        assertNotNull(userByUsername);
    }

    @Test
    @Order(5)
    void getUserByUsernameNotFoundException() {
        String username = "ghost";
        assertThatThrownBy(() -> underTest.getUserByUsername(username))
                .hasMessage("Get - User not found with username " + username);
    }

    @Test
    @Order(6)
    void deleteUser() {
        User user = User.builder()
                .id(1L)
                .username("jane")
                .email("jane@program.hr")
                .firstname("Jane")
                .lastname("Smith")
                .password("secret")
                .role(Role.USER)
                .build();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        underTest.deleteUser(user.getId());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).delete(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(user, capturedUser);

    }


}