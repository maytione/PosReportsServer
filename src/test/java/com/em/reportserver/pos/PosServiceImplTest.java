package com.em.reportserver.pos;


import com.em.reportserver.user.Role;
import com.em.reportserver.user.User;
import com.em.reportserver.user.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(value = "test")
class PosServiceImplTest {


    @Autowired
    private PosServiceImpl underTest;

    @Mock
    private PosRepository posRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        underTest = new PosServiceImpl(userRepository, posRepository);
    }


    @Test
    @Order(1)
    void createPos() {
        PosDto posDto = PosDto.builder()
                .name("My Test POS")
                .code("TEST")
                .build();

        Pos pos = underTest.createPos(posDto);
        assertThat(pos).isNotNull();
        assertEquals(pos.getName(), posDto.getName());
        assertEquals(pos.getCode(), posDto.getCode());
        assertThat(pos.getUser()).isNull();
        assertThat(pos.getOrders()).isNull();
        assertThat(pos.getId()).isEqualTo(null);
        assertThat(posDto.getCode()).isNotEmpty();
        assertThat(posDto.getName()).isNotEmpty();
    }

    @Test
    @Order(2)
    void assignPosToUser() {

        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setRole(Role.USER);
        userOptional.get().setUsername("user");
        userOptional.get().setPassword("password");
        given(userRepository.findById(1L)).willReturn(userOptional);


        Optional<Pos> posOptional = Optional.of(new Pos());
        posOptional.get().setId(1L);
        posOptional.get().setCode("MOCK");
        posOptional.get().setUser(userOptional.get());
        given(posRepository.findById(1L)).willReturn(posOptional);

        underTest.assignPosToUser(posOptional.get().getId(), userOptional.get().getId());
    }


    @Test
    @Order(3)
    void assignPosToInvalidUser() {
        Optional<Pos> posOptional = Optional.of(new Pos());
        posOptional.get().setId(1L);
        posOptional.get().setCode("MOCK");
        given(posRepository.findById(1L)).willReturn(posOptional);
        assertThatThrownBy(() -> underTest.assignPosToUser(posOptional.get().getId(), 99L))
                .hasMessage("User not found");
    }

    @Test
    @Order(4)
    void assignPosToInvalidPOS() {
        assertThatThrownBy(() -> underTest.assignPosToUser(99L, 1L))
                .hasMessage("POS not found");
    }


    @Test
    @Order(5)
    void getAllPos() {
        Pos pos = new Pos();
        pos.setId(1L);
        pos.setCode("MOCK");
        List<Pos> posList = new ArrayList<>();
        posList.add(pos);
        given(posRepository.findAll()).willReturn(posList);

        List<Pos> result = underTest.getAllPos();
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(List.class);
        assertThat(result).size().isGreaterThan(0);
    }

    @Test
    @Order(6)
    void getUserPos() {


        Pos pos = new Pos();
        pos.setId(1L);
        pos.setCode("MOCK");
        List<Pos> posList = new ArrayList<>();
        posList.add(pos);

        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setRole(Role.USER);
        userOptional.get().setUsername("user");
        userOptional.get().setPassword("password");
        userOptional.get().setPosList(posList);
        given(userRepository.findByUsername(userOptional.get().getUsername())).willReturn(userOptional);

        List<Pos> result = underTest.getUserPos(userOptional.get().getUsername());
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(List.class);
        assertThat(result).size().isGreaterThan(0);
    }

    @Test
    @Order(7)
    void getUserPosAndThrowException() {
        assertThatThrownBy(() -> underTest.getUserPos("ghost"))
                .hasMessage("User not found");
    }


    @Test
    @Order(8)
    void updatePos() {
        PosDto posDto = PosDto.builder()
                .name("My Test POS")
                .code("TST")
                .build();

        Optional<Pos> posOptional = Optional.of(new Pos());
        posOptional.get().setId(1L);
        posOptional.get().setCode("MOCK");
        given(posRepository.findById(1L)).willReturn(posOptional);

        assertThatNoException().isThrownBy(() -> underTest.updatePos(1L, posDto));
    }

    @Test
    @Order(8)
    void updateNonExistingPos() {
        PosDto posDto = PosDto.builder()
                .name("My Test POS")
                .code("TST")
                .build();
        assertThatThrownBy(() -> underTest.updatePos(99L, posDto))
                .hasMessage("POS not found");
    }

    @Test
    @Order(9)
    void deletePos() {
        assertThatNoException().isThrownBy(() -> underTest.deletePos(1L));
    }
}