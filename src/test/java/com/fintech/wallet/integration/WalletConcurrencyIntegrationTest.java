package com.fintech.wallet.integration;

import com.fintech.wallet.dto.CashInRequest;
import com.fintech.wallet.model.entity.Currency;
import com.fintech.wallet.model.entity.User;
import com.fintech.wallet.model.entity.Wallet;
import com.fintech.wallet.repository.CurrencyRepository;
import com.fintech.wallet.repository.UserRepository;
import com.fintech.wallet.repository.WalletRepository;
import com.fintech.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class WalletConcurrencyIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void shouldHandleConcurrentDepositsCorrectly() throws InterruptedException {
        // Arrange
        Currency mxn = currencyRepository.findById("MXN")
                .orElseGet(() -> currencyRepository.save(new Currency("MXN", "Peso Mexicano")));

        User user = new User("Concurrency", "Test", "concurrency@test.com", "pass123");
        Wallet wallet = new Wallet(user, mxn);
        user.setWallet(wallet);
        userRepository.save(user);

        UUID walletId = wallet.getId();
        BigDecimal depositAmount = new BigDecimal("10.00");
        int numberOfThreads = 4;
        int depositsPerThread = 100;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // Act
        System.out.println("🚀 Starting stress test...");
        CompletableFuture<?>[] futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    for (int j = 0; j < depositsPerThread; j++) {
                        walletService.deposit(new CashInRequest(walletId, depositAmount));
                    }
                }, executor))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Assert
        Wallet updatedWallet = walletRepository.findById(walletId).orElseThrow();

        BigDecimal expectedBalance = depositAmount.multiply(new BigDecimal(numberOfThreads * depositsPerThread));
        BigDecimal actualBalance = updatedWallet.getBalance();

        assertThat(expectedBalance).isEqualByComparingTo(actualBalance);
    }
}