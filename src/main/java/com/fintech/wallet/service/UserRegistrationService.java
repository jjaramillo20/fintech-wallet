package com.fintech.wallet.service;

import com.fintech.wallet.dto.UserRegistrationRequest;
import com.fintech.wallet.dto.UserResponse;
import com.fintech.wallet.exception.UserAlreadyExistsException;
import com.fintech.wallet.mapping.UserMapper;
import com.fintech.wallet.model.entity.Currency;
import com.fintech.wallet.model.entity.User;
import com.fintech.wallet.model.entity.Wallet;
import com.fintech.wallet.repository.CurrencyRepository;
import com.fintech.wallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())){
            throw new UserAlreadyExistsException("Email is in use");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.name(), request.lastName(), request.email(), encodedPassword);
        Currency currency = currencyRepository.findById(request.currency())
                .orElseThrow(() -> new IllegalArgumentException("Currency not supported: " + request.currency()));
        Wallet wallet = new Wallet(user, currency);
        user.setWallet(wallet);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
}
