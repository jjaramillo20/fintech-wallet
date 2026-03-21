package com.fintech.wallet.exception;

public class WalletNotExistsException extends RuntimeException {
    public WalletNotExistsException(String message) {
        super(message);
    }
}
