package com.fintech.wallet.unit.exception;

import com.fintech.wallet.dto.ErrorResponse;
import com.fintech.wallet.exception.GlobalExceptionHandler;
import com.fintech.wallet.exception.exceptions.UserAlreadyExistsException;
import com.fintech.wallet.exception.exceptions.WalletNotExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void shouldReturnErrorResponse_whenThrowWalletNotExistsException(){
        //Arrange
        WalletNotExistsException walletNotExistsException = new WalletNotExistsException("Wallet not found");

        //Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleWalletNotExistsException(walletNotExistsException);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().error()).isEqualTo("Not Found");
        assertThat(response.getBody().message()).isEqualTo("Wallet not found");
        assertThat(response.getBody().fieldErrors()).isNull();
    }

    @Test
    void shouldReturnErrorResponse_whenThrowUserAlreadyExistsException(){
        //Arrange
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Email is in use");

        //Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserAlreadyExistsException(userAlreadyExistsException);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().error()).isEqualTo("Conflict");
        assertThat(response.getBody().message()).isEqualTo("Email is in use");
        assertThat(response.getBody().fieldErrors()).isNull();
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("request", "email", "must not be blank");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("Request validation failed");
        assertThat(response.getBody().fieldErrors()).containsEntry("email", "must not be blank");
    }
}
