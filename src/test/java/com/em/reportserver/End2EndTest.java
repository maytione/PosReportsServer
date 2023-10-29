package com.em.reportserver;

import com.em.reportserver.auth.AuthenticationResponse;
import com.em.reportserver.order.OrderItemDto;
import com.em.reportserver.order.OrderType;
import com.em.reportserver.pos.PosDto;
import com.em.reportserver.pos.PosResponse;
import com.em.reportserver.user.Role;
import com.em.reportserver.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static com.em.reportserver.Utils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(value = "test")
public class End2EndTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private String willSmithToken;
    private long willSmithPosId;

    private String willSmithPosCode;

    private long willSmithId;

    public End2EndTest() {
    }


    @Test
    @WithUserDetails("admin")
    @Order(1)
    void adminShouldRegisterUser() throws Exception {

        UserDto userDto = UserDto.builder()
                .email("will.smith@program.hr")
                .username("will.smith")
                .firstname("Will")
                .lastname("Smith")
                .password("diehard")
                .role(Role.USER)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        AuthenticationResponse authenticationResponse = objectMapper.readValue(content, AuthenticationResponse.class);

        willSmithToken = authenticationResponse.getToken();
        willSmithId = authenticationResponse.getId();
    }

    @Test
    @DependsOn("adminShouldRegisterUser")
    @WithUserDetails("admin")
    @Order(2)
    void adminShouldCreatePos() throws Exception {
        PosDto posDto = PosDto.builder()
                .name("Will's POS")
                .code("POS1")
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/pos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(posDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        PosResponse posResponse = objectMapper.readValue(content, PosResponse.class);

        willSmithPosId = posResponse.getId();
        willSmithPosCode = posResponse.getCode();
    }

    @Test
    @DependsOn("adminShouldCreatePos")
    @WithUserDetails("admin")
    @Order(3)
    void adminShouldAssignPosToUser() throws Exception {
        PosDto posDto = PosDto.builder()
                .name("Will's POS")
                .code("POS1")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/pos/{pos_Id}/{user_id}", willSmithPosId, willSmithId)
                        .content(asJsonString(posDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DependsOn("adminShouldAssignPosToUser")
    @Order(4)
    void userShouldCreateOrder() throws Exception {
        OrderItemDto request = OrderItemDto.builder()
                .orderType(OrderType.REVENUE)
                .intervalFrom(LocalDateTime.now())
                .intervalTo(LocalDateTime.now())
                .posId(willSmithPosId)
                .build();
        mockMvc.perform(post("/api/v1/order")
                        .header("authorization", "Bearer " + willSmithToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @DependsOn("userShouldCreateOrder")
    @Order(5)
    void userShouldGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/v1/order")
                        .header("authorization", "Bearer " + willSmithToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DependsOn("userShouldGetAllOrdersByPosCode")
    @Order(6)
    void appShouldGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/v1/order/" + willSmithPosCode)
                        .header("authorization", "Bearer " + willSmithToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
