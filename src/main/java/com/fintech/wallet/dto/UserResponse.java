package com.fintech.wallet.dto;

import java.util.UUID;

public record UserResponse(UUID id, String name, String lastName, String email, WalletDto walletDto) {
}
