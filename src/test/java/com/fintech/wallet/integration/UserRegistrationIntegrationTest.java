package com.fintech.wallet.integration;

import com.fintech.wallet.model.entity.Currency;
import com.fintech.wallet.model.entity.User;
import com.fintech.wallet.model.entity.Wallet;
import com.fintech.wallet.repository.CurrencyRepository;
import com.fintech.wallet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserRegistrationIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserRepository userRepository; 

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void shouldCreateUserWithWalletAndCurrency() {
        // Arrange
        Currency mxn = new Currency("MXN", "Peso Mexicano");
        currencyRepository.save(mxn);

        User newUser = new User("Roberto", "Gomez", "roberto@test.com", "hash_seguro_123");

        Wallet newWallet = new Wallet(newUser, mxn);

        newUser.setWallet(newWallet);

        // Act
        User savedUser = userRepository.save(newUser);

        // Assert        
        assertThat(savedUser.getId()).isNotNull();

        Optional<User> fetchedUserOpt = userRepository.findById(savedUser.getId());        
        assertThat(fetchedUserOpt).isPresent();

        User fetchedUser = fetchedUserOpt.get();
        
        assertThat(fetchedUser.getWallet()).isNotNull();
        assertThat(fetchedUser.getWallet().getBalance()).isEqualByComparingTo(new BigDecimal("0.0000"));
        assertThat(fetchedUser.getWallet().getCurrency().getId()).isEqualTo("MXN");
    }
}
