package com.fintech.wallet.exception.exceptions;

public class WalletNotExistsException extends RuntimeException {
    public WalletNotExistsException(String message) {
        super(message);
    }
}
