package com.fintech.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CashInRequest(@NotNull(message = "Wallet ID is required")
                            UUID walletId,

                            @NotNull(message = "The amount is mandatory")
                            @DecimalMin(value = "0.01", message = "The minimum deposit amount is 0.01")
                            @Digits(integer = 15, fraction = 4, message = "Invalid amount format (max 15 whole numbers and 4 decimal places)")
                            BigDecimal amount) {
}
