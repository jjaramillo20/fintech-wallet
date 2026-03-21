package com.fintech.wallet.controller;

import com.fintech.wallet.dto.UserRegistrationRequest;
import com.fintech.wallet.dto.UserResponse;
import com.fintech.wallet.service.UserRegistrationService;
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

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations related to users and onboarding")
public class UserController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping
    @Operation(
            summary = "Register a new user",
            description = "Creates a user along with an initial wallet in the selected currency (MXN, USD)."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User successfully created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input data (weak password, malformed email, unsupported currency)",
            content = @Content
    )
    @ApiResponse(
            responseCode = "409",
            description = "Email is already registered",
            content = @Content
    )
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request) {
        UserResponse response = userRegistrationService.register(request);

        return ResponseEntity
                .created(URI.create("/api/v1/users/" + response.id()))
                .body(response);
    }
}
