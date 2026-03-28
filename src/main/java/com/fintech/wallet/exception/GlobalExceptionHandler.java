package com.fintech.wallet.exception;

import com.fintech.wallet.dto.ErrorResponse;
import com.fintech.wallet.exception.exceptions.UserAlreadyExistsException;
import com.fintech.wallet.exception.exceptions.WalletNotExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WalletNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleWalletNotExistsException(final WalletNotExistsException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                errors
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                null
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(final HttpStatus status, final String message, final Map<String, String> fieldErrors) {
        final ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message != null ? message : "Unexpected error",
                fieldErrors
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}