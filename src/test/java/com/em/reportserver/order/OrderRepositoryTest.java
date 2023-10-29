package com.em.reportserver.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@ActiveProfiles(value = "test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository underTest;


    @Test
    void itShouldFindAllByUserId() {

        //give
        Long userId=1L;
        //when
        Optional<List<OrderItem>> result =underTest.findAllByUserId(userId);
        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get()).isInstanceOf(List.class);
        assertThat(result.get()).size().isEqualTo(0);



    }

}