package com.em.reportserver.admin;

import com.em.reportserver.pos.PosDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
class AdminPosControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("GET /api/v1/admin/pos")
    @WithUserDetails("admin")
    void getAllPos() throws Exception {
        mockMvc.perform(get("/api/v1/admin/pos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/v1/admin/pos")
    @WithUserDetails("admin")
    void createPos() throws Exception {
        PosDto posDto = PosDto.builder()
                .name("My POS")
                .code("MOCK")
                .build();
        mockMvc.perform(post("/api/v1/admin/pos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(posDto))
                )
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("PUT /api/v1/admin/pos/{pos_id}/{user_id}")
    @WithUserDetails("admin")
    void assignPosToUser() throws Exception {
        PosDto posDto = PosDto.builder()
                .name("My POS")
                .code("MOCK")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/pos/{pos_Id}/{user_id}", 1, 1)
                        .content(asJsonString(posDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(4)
    @DisplayName("PUT /api/v1/admin/pos/{pos_Id}/{user_id} NotFoundException")
    @WithUserDetails("admin")
    void assignPosToUserPosNotFoundException() throws Exception {
        PosDto posDto = PosDto.builder()
                .name("My POS")
                .code("MOCK")
                .build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/pos/{pos_Id}/{user_id}", 99, 1)
                        .content(asJsonString(posDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Optional<RuntimeException> exception = Optional.ofNullable((RuntimeException) mvcResult.getResolvedException());
        assertTrue(exception.isPresent());
        assertEquals(exception.get().getMessage(), "POS not found");
    }

    @Test
    @Order(5)
    @DisplayName("PUT /api/v1/admin/pos/{pos_id}")
    @WithUserDetails("admin")
    void updatePos() throws Exception {
        PosDto posDto = PosDto.builder()
                .name("My POS")
                .code("MOCK")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/pos/{pos_Id}", 1)
                        .content(asJsonString(posDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @Order(6)
    @DisplayName("PUT /api/v1/admin/pos/{pos_id} NotFoundException")
    @WithUserDetails("admin")
    void updatePosNotFoundException() throws Exception {
        PosDto posDto = PosDto.builder()
                .name("My POS")
                .code("MOCK")
                .build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/pos/{pos_Id}", 99)
                        .content(asJsonString(posDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Optional<RuntimeException> exception = Optional.ofNullable((RuntimeException) mvcResult.getResolvedException());
        assertTrue(exception.isPresent());
        assertEquals(exception.get().getMessage(), "POS not found");

    }

    @Test
    @Order(7)
    @DisplayName("DELETE /api/v1/admin/pos/{pos_id}")
    @WithUserDetails("admin")
    void deletePos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/admin/pos/{pos_id}", 1))
                .andExpect(status().isOk());

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