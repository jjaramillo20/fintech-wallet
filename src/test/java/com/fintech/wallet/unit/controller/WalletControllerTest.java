package com.fintech.wallet.unit.controller;

import com.fintech.wallet.dto.CashInRequest;
import com.fintech.wallet.dto.WalletResponse;
import com.fintech.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tools.jackson.databind.json.JsonMapper;
import com.fintech.wallet.controller.WalletController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private WalletService walletService;

    @Test
    void shouldReturnOk_whenDepositIsSuccessful() throws Exception {
        // Arrange
        UUID walletId = UUID.randomUUID();
        CashInRequest request = new CashInRequest(walletId, new BigDecimal("300.00"));
        WalletResponse response = new WalletResponse(walletId, new BigDecimal("1300.00"));

        when(walletService.deposit(any(CashInRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/wallets/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1300.00));
    }

    @Test
    void shouldReturnBadRequest_whenAmountIsInvalid() throws Exception {
        // Arrange
        CashInRequest invalidRequest = new CashInRequest(UUID.randomUUID(), new BigDecimal("-50.00"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/wallets/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}
