package com.fintech.wallet.mapping;

import com.fintech.wallet.dto.WalletResponse;
import com.fintech.wallet.model.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class WalletMapper {

    public WalletResponse toResponse(Wallet wallet){
        Objects.requireNonNull(wallet, "Wallet cannot be null or empty");
        if (wallet.getBalance() == null || wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The balance must be greater than zero");
        }
        return new WalletResponse(
                wallet.getId(),
                wallet.getBalance()
        );
    }
}
