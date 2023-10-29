package com.em.reportserver.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(value = "test")
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    @Order(1)
    @DisplayName("POST /api/v1/order")
    @WithMockUser("admin")
    void createOrder() throws Exception {
        OrderItemDto request = OrderItemDto.builder()
                .orderType(OrderType.REVENUE)
                .intervalFrom(LocalDateTime.now())
                .intervalTo(LocalDateTime.now())
                .posId(1L)
                .build();
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Optional<RuntimeException> exception = Optional.ofNullable((RuntimeException) mvcResult.getResolvedException());
        assertTrue(exception.isPresent());
        assertEquals(exception.get().getMessage(), "POS not found");

    }


    @Test
    @Order(2)
    @DisplayName("GET /api/v1/order")
    @WithMockUser("admin")
    void getOrders() throws Exception {

        mockMvc.perform(get("/api/v1/order"))
                .andExpect(status().isOk());

    }


    @Test
    @Order(3)
    @DisplayName("GET /api/v1/order/MOCK")
    @WithMockUser("admin")
    void getOrderJobs() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/order/MOCK")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        Optional<RuntimeException> exception = Optional.ofNullable((RuntimeException) mvcResult.getResolvedException());
        assertTrue(exception.isPresent());
        assertEquals(exception.get().getMessage(), "POS not found or not assigned to user");


    }

    static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}