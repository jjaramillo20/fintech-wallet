package com.fintech.wallet.unit.controller;



import com.fintech.wallet.controller.UserController;
import com.fintech.wallet.dto.UserRegistrationRequest;
import com.fintech.wallet.dto.UserResponse;
import com.fintech.wallet.dto.WalletDto;
import com.fintech.wallet.service.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private UserRegistrationService userRegistrationService;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Rodrigo", "Sarabia", "rodrigo@test.com", "securePass123", "MXN");

        UserResponse dummyResponse = new UserResponse(UUID.randomUUID(), "Rodrigo", "Sarabia", "rodrigo@test.com", new WalletDto(UUID.randomUUID(),BigDecimal.ZERO, "MXN"));
        when(userRegistrationService.register(any())).thenReturn(dummyResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.email").value("rodrigo@test.com"));
    }

    @Test
    void shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {
        // Arrange
        UserRegistrationRequest invalidRequest = new UserRegistrationRequest(
                "Rodrigo", "Sarabia", "", "pass", "MXN");

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}