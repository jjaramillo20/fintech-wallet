package com.fintech.wallet.unit.service;

import com.fintech.wallet.dto.CashInRequest;
import com.fintech.wallet.dto.WalletResponse;
import com.fintech.wallet.exception.WalletNotExistsException;
import com.fintech.wallet.mapping.WalletMapper;
import com.fintech.wallet.model.entity.Currency;
import com.fintech.wallet.model.entity.User;
import com.fintech.wallet.model.entity.Wallet;
import com.fintech.wallet.repository.WalletRepository;
import com.fintech.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Spy
    private WalletMapper walletMapper = new WalletMapper();

    @InjectMocks
    private WalletService walletService;

    private UUID invalidWalletId;
    private UUID validWalletId;

    private CashInRequest validDepositRequest;
    private CashInRequest invalidDepositRequest;

    private User user;

    private Currency currency;

    private Wallet wallet;

    @BeforeEach
    void setUp(){
        invalidWalletId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        validWalletId = UUID.fromString("440e8400-e29b-41d4-a716-446655448888");
        invalidDepositRequest = new CashInRequest(invalidWalletId, new BigDecimal("100.00"));
        validDepositRequest = new CashInRequest(validWalletId, new BigDecimal("100.00"));
        user = new User("Rodrigo", "Sarabia", "rsarabia@test.com", "pass1234");
        currency = new Currency("MXN", "Pesos Mexicanos");
        wallet = new Wallet(user, currency);
    }

    @Test
    void shouldThrowWalletNotExistsException_whenWalletIdDoesNotExist(){
        // Arrange
        when(walletRepository.findByIdWithLock(invalidWalletId)).thenThrow(new WalletNotExistsException("Wallet not found"));

        // Act & Assert
        assertThatThrownBy(() -> walletService.deposit(invalidDepositRequest))
                .isInstanceOf(WalletNotExistsException.class)
                .hasMessage("Wallet not found");
    }

    @Test
    void shouldIncreaseWalletBalance_whenDepositIsSuccessful(){
        // Arrange
        when(walletRepository.findByIdWithLock(validWalletId)).thenReturn(Optional.of(wallet));

        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Act
        WalletResponse walletResponse = walletService.deposit(validDepositRequest);

        // Assert
        assertThat(wallet.getBalance()).isNotEqualByComparingTo(BigDecimal.ZERO);
        assertThat(wallet.getBalance()).isEqualByComparingTo("100.0000");
        assertThat(walletResponse.balance()).isEqualByComparingTo("100.0000");

    }
}
