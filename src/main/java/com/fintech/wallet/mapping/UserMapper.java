package com.fintech.wallet.mapping;

import com.fintech.wallet.dto.UserResponse;
import com.fintech.wallet.dto.WalletDto;
import com.fintech.wallet.model.entity.User;
import com.fintech.wallet.model.entity.Wallet;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        Objects.requireNonNull(user, "User to map cannot be null or empty");
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                toWalletDto(user.getWallet()) // Mapeo anidado
        );
    }

    private WalletDto toWalletDto(Wallet wallet) {
        if (wallet == null) throw new IllegalArgumentException("The Wallet cannot be null or empty");
        return new WalletDto(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getCurrency().getId()
        );
    }
}