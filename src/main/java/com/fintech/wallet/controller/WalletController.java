package com.fintech.wallet.controller;

import com.fintech.wallet.dto.CashInRequest;
import com.fintech.wallet.dto.WalletResponse;
import com.fintech.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallets", description = "Operations related to wallets and deposits")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/deposit")
    @Operation(
            summary = "Perform a wallet deposit",
            description = "Increases the balance of a specific wallet. This operation is atomic and uses pessimistic locking to ensure consistency."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Deposit successfully processed. Returns the updated wallet state.",
            content = @Content(
                    schema = @Schema(implementation = WalletResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid amount or validation error",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Wallet not found",
            content = @Content
    )
    @ApiResponse(
            responseCode = "409",
            description = "Transaction conflict (possible race condition detected)",
            content = @Content
    )
    public ResponseEntity<WalletResponse> deposit(@RequestBody @Valid CashInRequest cashInRequest) {
        return ResponseEntity.ok(walletService.deposit(cashInRequest));
    }
}
