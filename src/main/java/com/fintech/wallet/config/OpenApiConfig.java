package com.fintech.wallet.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Fintech Wallet API",
                version = "1.0",
                description = "API for managing digital wallets and financial transactions."
        )
)
public class OpenApiConfig {
}