# 💳 Fintech Wallet API

REST API for digital wallet management, built with Java and Spring Boot.

## 🚀 Tech Stack

- **Java 21** + **Spring Boot 3**
- **PostgreSQL** — relational database
- **Flyway** — database migrations
- **Docker Compose** — local environment setup
- **Testcontainers** — integration testing
- **OpenAPI / Swagger** — API documentation

## 📋 Features

- User registration and profile management
- Wallet creation per user with multi-currency support
- Cash-in (deposit) operations
- Encrypted sensitive data

## ▶️ Getting Started

### Prerequisites

- Java 21
- Docker and Docker Compose

### Run locally
```bash
# Start PostgreSQL
docker-compose up -d

# Run the application
./mvnw spring-boot:run
```

### API Docs

Once running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## 🧪 Running Tests
```bash
./mvnw test
```

> Integration tests use Testcontainers — Docker must be running.

## 📁 Project Structure
```
src/
├── main/
│   ├── java/com/fintech/wallet/
│   │   ├── config/        # Security and OpenAPI config
│   │   ├── controller/    # REST endpoints
│   │   ├── dto/           # Request and response objects
│   │   ├── exception/     # Custom exceptions
│   │   ├── mapping/       # Entity-DTO mappers
│   │   ├── model/entity/  # JPA entities
│   │   ├── repository/    # Spring Data repositories
│   │   └── service/       # Business logic
│   └── resources/
│       ├── application.yaml
│       └── db/migration/  # Flyway scripts
└── test/
    ├── integration/       # Integration tests
    └── unit/              # Unit tests
```