package com.fintech.wallet.service;

import com.fintech.wallet.dto.CashInRequest;
import com.fintech.wallet.dto.WalletResponse;
import com.fintech.wallet.exception.exceptions.WalletNotExistsException;
import com.fintech.wallet.mapping.WalletMapper;
import com.fintech.wallet.model.entity.Wallet;
import com.fintech.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Transactional
    public WalletResponse deposit(CashInRequest cashInRequest) {
        Wallet wallet = walletRepository.findByIdWithLock(cashInRequest.walletId())
                .orElseThrow(() -> new WalletNotExistsException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(cashInRequest.amount()));
        Wallet savedWallet = walletRepository.save(wallet);
        return walletMapper.toResponse(savedWallet);
    }
}
