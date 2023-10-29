package com.em.reportserver.order;


import com.em.reportserver.pos.Pos;
import com.em.reportserver.pos.PosRepository;
import com.em.reportserver.user.Role;
import com.em.reportserver.user.User;
import com.em.reportserver.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PosRepository posRepository;
    @Mock
    private OrderRepository orderRepository;

    private OrderServiceImpl underTest;


    @BeforeEach
    void setUp() {
        underTest = new OrderServiceImpl(userRepository, posRepository, orderRepository);
    }


    @Test
    void createOrder() {
        OrderItemDto orderRequest = OrderItemDto.builder()
                .orderType(OrderType.REVENUE)
                .intervalFrom(LocalDateTime.now())
                .intervalTo(LocalDateTime.now())
                .posId(1L)
                .build();


        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setRole(Role.USER);
        userOptional.get().setUsername("user");
        userOptional.get().setPassword("password");
        given(userRepository.findByUsername("user")).willReturn(userOptional);


        Optional<Pos> posOptional = Optional.of(new Pos());
        posOptional.get().setId(1L);
        posOptional.get().setCode("MOCK");
        posOptional.get().setUser(userOptional.get());
        given(posRepository.findById(orderRequest.getPosId())).willReturn(posOptional);

        underTest.createOrder("user", orderRequest);

        ArgumentCaptor<OrderItem> orderItemArgumentCaptor = ArgumentCaptor.forClass(OrderItem.class);

        verify(orderRepository).save(orderItemArgumentCaptor.capture());

        OrderItem capturedOrderItem = orderItemArgumentCaptor.getValue();

        assertEquals(capturedOrderItem.getOrderType(), orderRequest.getOrderType());


    }

    @Test
    void createOrderUserNotFoundException() {

        OrderItemDto orderRequest = OrderItemDto.builder()
                .orderType(OrderType.REVENUE)
                .intervalFrom(LocalDateTime.now())
                .intervalTo(LocalDateTime.now())
                .posId(1L)
                .build();

        assertThatThrownBy(() -> underTest.createOrder("ghost", orderRequest))
                .hasMessage("User not found");


    }

    @Test
    void createOrderPosNotFoundException() {

        OrderItemDto orderRequest = OrderItemDto.builder()
                .orderType(OrderType.REVENUE)
                .intervalFrom(LocalDateTime.now())
                .intervalTo(LocalDateTime.now())
                .posId(1L)
                .build();

        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setRole(Role.USER);
        userOptional.get().setUsername("user");
        userOptional.get().setPassword("password");
        given(userRepository.findByUsername("user")).willReturn(userOptional);

        assertThatThrownBy(() -> underTest.createOrder("user", orderRequest))
                .hasMessage("POS not found");

    }

    @Test
    void createOrderPosNotAssignedException() {

        OrderItemDto orderRequest = OrderItemDto.builder()
                .orderType(OrderType.REVENUE)
                .intervalFrom(LocalDateTime.now())
                .intervalTo(LocalDateTime.now())
                .posId(1L)
                .build();

        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setRole(Role.USER);
        userOptional.get().setUsername("user");
        userOptional.get().setPassword("password");
        given(userRepository.findByUsername("user")).willReturn(userOptional);

        Optional<Pos> posOptional = Optional.of(new Pos());
        posOptional.get().setId(1L);
        posOptional.get().setCode("MOCK");
        posOptional.get().setUser(null);
        given(posRepository.findById(orderRequest.getPosId())).willReturn(posOptional);


        assertThatThrownBy(() -> underTest.createOrder("user", orderRequest))
                .hasMessage("POS not assigned");


    }


    @Test
    void getOrders() {

        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setEmail("mocked@program.hr");
        given(userRepository.findByUsername("admin")).willReturn(userOptional);

        underTest.getOrders("admin");
        verify(orderRepository).findAllByUserId(userOptional.get().getId());
    }

    @Test
    void getOrdersUserNotFoundException() {

        assertThatThrownBy(() -> underTest.getOrders("ghost")).hasMessage("User not found");
    }


    @Test
    void getOrderJobs() {

        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setRole(Role.USER);
        userOptional.get().setUsername("user");
        userOptional.get().setPassword("password");
        given(userRepository.findByUsername("user")).willReturn(userOptional);


        Optional<Pos> posOptional = Optional.of(new Pos());
        posOptional.get().setId(1L);
        posOptional.get().setCode("MOCK");
        given(posRepository.findByUserIdAndCode(1L, "MOCK")).willReturn(posOptional);


        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(OrderItem.builder()
                .id(1L)
                .orderType(OrderType.REVENUE)
                .intervalFrom(LocalDateTime.now())
                .userId(1L)
                .posId(1L)
                .intervalTo(LocalDateTime.now())
                .reportData(null)
                .build()
        );
        when(orderRepository.findAllByUserIdAndPosIdAndReportData(1L, 1L, null))
                .thenReturn(Optional.of(orderItemList));

        List<OrderItem> orderJobs = underTest.getOrderJobs("user", "MOCK");

        assertThat(orderJobs).isInstanceOf(List.class);
        assertThat(orderJobs).size().isGreaterThan(0);
        assertThat(orderJobs.get(0)).isInstanceOf(OrderItem.class);
        assertThat(orderJobs.get(0).getId()).isEqualTo(orderItemList.get(0).getId());
        assertThat(orderJobs.get(0).getOrderType()).isEqualTo(orderItemList.get(0).getOrderType());
        assertThat(orderJobs.get(0).getIntervalTo()).isEqualTo(orderItemList.get(0).getIntervalTo());
        assertThat(orderJobs.get(0).getIntervalFrom()).isEqualTo(orderItemList.get(0).getIntervalFrom());
        assertThat(orderJobs.get(0).getUserId()).isEqualTo(orderItemList.get(0).getUserId());
        assertThat(orderJobs.get(0).getPosId()).isEqualTo(orderItemList.get(0).getPosId());
        assertThat(orderJobs.get(0).getReportData()).isEqualTo(orderItemList.get(0).getReportData());
    }

    @Test
    void getOrderJobsUserNotFoundException() {
        assertThatThrownBy(() -> underTest.getOrderJobs("ghost", "MOCK"))
                .hasMessage("User not found");
    }

    @Test
    void getOrderJobsPosNotFoundException() {

        Optional<User> userOptional = Optional.of(new User());
        userOptional.get().setId(1L);
        userOptional.get().setEmail("mocked@program.hr");
        given(userRepository.findByUsername("admin")).willReturn(userOptional);
        assertThatThrownBy(() -> underTest.getOrderJobs("admin", "MOCK"))
                .hasMessage("POS not found or not assigned to user");
    }
}