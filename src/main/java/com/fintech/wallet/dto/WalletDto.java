package com.fintech.wallet.dto;

import com.fintech.wallet.model.entity.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDto(UUID walletId, BigDecimal balance, String currency) {
}
