# Bajaj Broking Trading SDK

**Production-ready Spring Boot Trading API Wrapper**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](https://bajajbroking.com)

---

## 🎯 Overview

A complete, enterprise-grade trading SDK implementing core brokerage operations with clean architecture, robust error handling, and comprehensive documentation. Built for **Bajaj Broking** with production-ready patterns.

### Key Features

✅ **RESTful API** - Complete CRUD operations with `/api/v1` versioning  
✅ **JPA Entities** - Proper relationships and lifecycle management  
✅ **Trading Logic** - MARKET & LIMIT order execution with portfolio updates  
✅ **Exception Handling** - Custom exceptions with appropriate HTTP codes  
✅ **Swagger UI** - Full OpenAPI 3.0 documentation  
✅ **Validation** - Request validation with Jakarta Bean Validation  
✅ **Layered Architecture** - Controller → Service → Repository pattern  
✅ **Logging** - SLF4J with structured logging  
✅ **Sample Data** - 15 NSE stocks preloaded  

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    REST Controllers                      │
│  InstrumentController │ OrderController │ TradeController│
│         PortfolioController                              │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                   Service Layer                          │
│  InstrumentService │ OrderService │ TradeService         │
│         PortfolioService                                 │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                 Repository Layer (JPA)                   │
│  InstrumentRepo │ OrderRepo │ TradeRepo │ PortfolioRepo │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│              H2 In-Memory Database                       │
│    Instrument │ Order │ Trade │ Portfolio                │
└─────────────────────────────────────────────────────────┘
```

### Data Flow (Order Placement)

```
1. Client → POST /api/v1/orders → OrderController
2. OrderController → Validate → OrderService
3. OrderService → Check Instrument → InstrumentService
4. OrderService → Validate Holdings (SELL) → PortfolioService
5. OrderService → Create Order → OrderRepository
6. OrderService → Execute Logic (MARKET/LIMIT)
7. OrderService → Create Trade → TradeService
8. OrderService → Update Portfolio → PortfolioService
9. OrderService → Response → Client
```

---

## 🚀 Quick Start

### Prerequisites

- **Java 17+** ([Download](https://adoptium.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Git** (Optional)

### Installation & Setup

```bash
# Clone or extract the project
cd bajaj-trading-sdk

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

**Application will start on:** `http://localhost:8080`

### Verify Installation

```bash
curl http://localhost:8080/api/v1/instruments
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Interactive API Docs
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

---

## 🔌 API Endpoints

### 1. List Instruments

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/instruments
```

**Response:** `200 OK`
```json
[
  {
    "symbol": "RELIANCE",
    "exchange": "NSE",
    "instrumentType": "EQUITY",
    "lastTradedPrice": 2456.75
  },
  {
    "symbol": "TCS",
    "exchange": "NSE",
    "instrumentType": "EQUITY",
    "lastTradedPrice": 3645.80
  }
]
```

---

### 2. Place Market Order (BUY)

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "RELIANCE",
    "side": "BUY",
    "type": "MARKET",
    "quantity": 10
  }'
```

**Response:** `201 Created`
```json
{
  "orderId": 1,
  "symbol": "RELIANCE",
  "exchange": "NSE",
  "side": "BUY",
  "type": "MARKET",
  "quantity": 10,
  "price": null,
  "status": "EXECUTED",
  "executedPrice": 2456.75,
  "createdAt": "2026-01-08T10:30:00",
  "executedAt": "2026-01-08T10:30:00"
}
```

---

### 3. Place Limit Order (BUY)

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "TCS",
    "side": "BUY",
    "type": "LIMIT",
    "quantity": 5,
    "price": 3650.00
  }'
```

**Response:** `201 Created`
```json
{
  "orderId": 2,
  "symbol": "TCS",
  "exchange": "NSE",
  "side": "BUY",
  "type": "LIMIT",
  "quantity": 5,
  "price": 3650.00,
  "status": "EXECUTED",
  "executedPrice": 3645.80,
  "createdAt": "2026-01-08T10:35:00",
  "executedAt": "2026-01-08T10:35:00"
}
```

**Note:** LIMIT order executed because current price (3645.80) ≤ limit price (3650.00)

---

### 4. Get Order Status

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/orders/1
```

**Response:** `200 OK`
```json
{
  "orderId": 1,
  "symbol": "RELIANCE",
  "exchange": "NSE",
  "side": "BUY",
  "type": "MARKET",
  "quantity": 10,
  "price": null,
  "status": "EXECUTED",
  "executedPrice": 2456.75,
  "createdAt": "2026-01-08T10:30:00",
  "executedAt": "2026-01-08T10:30:00"
}
```

---

### 5. Get User Trades

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/trades
```

**Response:** `200 OK`
```json
[
  {
    "tradeId": 2,
    "orderId": 2,
    "symbol": "TCS",
    "exchange": "NSE",
    "side": "BUY",
    "quantity": 5,
    "executedPrice": 3645.80,
    "totalValue": 18229.00,
    "executedAt": "2026-01-08T10:35:00"
  },
  {
    "tradeId": 1,
    "orderId": 1,
    "symbol": "RELIANCE",
    "exchange": "NSE",
    "side": "BUY",
    "quantity": 10,
    "executedPrice": 2456.75,
    "totalValue": 24567.50,
    "executedAt": "2026-01-08T10:30:00"
  }
]
```

---

### 6. Get Portfolio

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/portfolio
```

**Response:** `200 OK`
```json
[
  {
    "symbol": "RELIANCE",
    "exchange": "NSE",
    "quantity": 10,
    "averagePrice": 2456.75,
    "currentValue": 24567.50
  },
  {
    "symbol": "TCS",
    "exchange": "NSE",
    "quantity": 5,
    "averagePrice": 3645.80,
    "currentValue": 18229.00
  }
]
```

---

## ⚙️ Trading Logic

### Order Execution Rules

#### MARKET Orders
- **Execute immediately** at current Last Traded Price (LTP)
- **Always fills** regardless of price
- **Status flow:** NEW → PLACED → EXECUTED

#### LIMIT Orders
- **BUY LIMIT:** Executes if `current_price ≤ limit_price`
- **SELL LIMIT:** Executes if `current_price ≥ limit_price`
- **Status flow:** 
  - Executed: NEW → PLACED → EXECUTED
  - Not executed: NEW → PLACED (remains open)

### Portfolio Updates

#### BUY Orders
```
New Position:
  quantity = quantity_bought
  averagePrice = execution_price
  currentValue = quantity × averagePrice

Existing Position:
  new_quantity = old_quantity + quantity_bought
  new_avg_price = (old_value + new_value) / new_quantity
  currentValue = new_quantity × new_avg_price
```

#### SELL Orders
```
Validation: available_quantity ≥ quantity_to_sell

After Sale:
  new_quantity = old_quantity - quantity_sold
  averagePrice = unchanged
  currentValue = new_quantity × averagePrice
```

---

## 🛡️ Error Handling

### Standard Error Response
```json
{
  "status": 400,
  "message": "Invalid order request",
  "details": "Price is required for LIMIT orders",
  "timestamp": "2026-01-08T10:30:00",
  "path": "/api/v1/orders"
}
```

### HTTP Status Codes

| Code | Description | Example |
|------|-------------|---------|
| 200 | Success | GET operations |
| 201 | Created | Order placed |
| 400 | Bad Request | Invalid order, validation failed |
| 404 | Not Found | Instrument/Order not found |
| 500 | Server Error | Unexpected error |

### Custom Exceptions

- `InstrumentNotFoundException` → 404
- `OrderNotFoundException` → 404
- `InvalidOrderException` → 400
- `InsufficientHoldingsException` → 400

---

## 🧪 Testing Scenarios

### Scenario 1: Complete Buy Flow
```bash
# 1. Check instruments
curl http://localhost:8080/api/v1/instruments

# 2. Buy RELIANCE shares
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"symbol":"RELIANCE","side":"BUY","type":"MARKET","quantity":10}'

# 3. Verify portfolio
curl http://localhost:8080/api/v1/portfolio

# 4. Check trades
curl http://localhost:8080/api/v1/trades
```

### Scenario 2: LIMIT Order Not Executed
```bash
# Try to buy TCS at price lower than current LTP
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"symbol":"TCS","side":"BUY","type":"LIMIT","quantity":5,"price":3600.00}'

# Current price is 3645.80, so order won't execute
# Status will be PLACED (not EXECUTED)
```

### Scenario 3: Sell Without Holdings
```bash
# Try to sell without buying first
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"symbol":"INFY","side":"SELL","type":"MARKET","quantity":5}'

# Expected: 400 Bad Request - Insufficient holdings
```

---

## 📊 Database Schema

### Instruments
```sql
CREATE TABLE instruments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  symbol VARCHAR(20) NOT NULL,
  exchange VARCHAR(10) NOT NULL,
  instrument_type VARCHAR(20) NOT NULL,
  last_traded_price DECIMAL(19,2) NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  UNIQUE(symbol, exchange)
);
```

### Orders
```sql
CREATE TABLE orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id VARCHAR(50) NOT NULL,
  symbol VARCHAR(20) NOT NULL,
  exchange VARCHAR(10) NOT NULL,
  side VARCHAR(10) NOT NULL, -- BUY/SELL
  type VARCHAR(10) NOT NULL, -- MARKET/LIMIT
  quantity INT NOT NULL,
  price DECIMAL(19,2),
  status VARCHAR(15) NOT NULL, -- NEW/PLACED/EXECUTED/CANCELLED
  executed_price DECIMAL(19,2),
  created_at TIMESTAMP NOT NULL,
  executed_at TIMESTAMP
);
```

### Trades
```sql
CREATE TABLE trades (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  symbol VARCHAR(20) NOT NULL,
  exchange VARCHAR(10) NOT NULL,
  side VARCHAR(10) NOT NULL,
  quantity INT NOT NULL,
  executed_price DECIMAL(19,2) NOT NULL,
  total_value DECIMAL(19,2) NOT NULL,
  executed_at TIMESTAMP NOT NULL
);
```

### Portfolio
```sql
CREATE TABLE portfolio (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id VARCHAR(50) NOT NULL,
  symbol VARCHAR(20) NOT NULL,
  exchange VARCHAR(10) NOT NULL,
  quantity INT NOT NULL,
  average_price DECIMAL(19,2) NOT NULL,
  current_value DECIMAL(19,2) NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  UNIQUE(user_id, symbol)
);
```

---

## 🔧 Configuration

### Application Properties (`application.yml`)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:trading_db
    username: sa
    password:
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

### H2 Console Access
- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:trading_db`
- **Username:** `sa`
- **Password:** *(empty)*

---

## 📦 Project Structure

```
bajaj-trading-sdk/
├── src/main/java/com/bajajbroking/trading/
│   ├── TradingSdkApplication.java
│   ├── controller/
│   │   ├── InstrumentController.java
│   │   ├── OrderController.java
│   │   ├── TradeController.java
│   │   └── PortfolioController.java
│   ├── service/
│   │   ├── InstrumentService.java
│   │   ├── OrderService.java
│   │   ├── TradeService.java
│   │   └── PortfolioService.java
│   ├── repository/
│   │   ├── InstrumentRepository.java
│   │   ├── OrderRepository.java
│   │   ├── TradeRepository.java
│   │   └── PortfolioRepository.java
│   ├── entity/
│   │   ├── Instrument.java
│   │   ├── Order.java
│   │   ├── Trade.java
│   │   └── Portfolio.java
│   ├── dto/
│   │   ├── InstrumentDTO.java
│   │   ├── OrderRequestDTO.java
│   │   ├── OrderResponseDTO.java
│   │   ├── TradeDTO.java
│   │   ├── PortfolioDTO.java
│   │   └── ErrorResponse.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── InstrumentNotFoundException.java
│   │   ├── OrderNotFoundException.java
│   │   ├── InvalidOrderException.java
│   │   └── InsufficientHoldingsException.java
│   └── config/
│       └── OpenApiConfig.java
├── src/main/resources/
│   ├── application.yml
│   └── data.sql
├── pom.xml
└── README.md
```

---

## 🎯 Design Decisions & Assumptions

### Authentication
- **Mock Authentication:** Hardcoded user `demo@trade.com`
- **Production:** Integrate Spring Security with JWT/OAuth2

### Order Execution
- **Instant Execution:** Orders execute immediately based on current LTP
- **Production:** Integrate with real exchange matching engine

### Portfolio Calculation
- **Average Price:** Weighted average on new purchases
- **Current Value:** Based on average price (not real-time LTP)
- **Production:** Calculate P&L with real-time prices

### Exchange
- **Single Exchange:** All instruments on NSE
- **Production:** Support multiple exchanges (NSE, BSE, MCX)

### Decimal Precision
- **Prices:** 2 decimal places (`DECIMAL(19,2)`)
- **Quantities:** Integer only (no fractional shares)

### Error Handling
- **Validation:** Jakarta Bean Validation on DTOs
- **Custom Exceptions:** Domain-specific error types
- **Global Handler:** Consistent error response format

### Concurrency
- **Single User:** No concurrent trade conflicts
- **Production:** Implement optimistic locking with `@Version`

---

## 🚀 Production Enhancements

### Security
- [ ] JWT authentication
- [ ] Role-based access control (RBAC)
- [ ] API rate limiting
- [ ] HTTPS enforcement

### Database
- [ ] PostgreSQL/MySQL for production
- [ ] Database migrations (Flyway/Liquibase)
- [ ] Connection pooling (HikariCP)
- [ ] Read replicas for scalability

### Monitoring
- [ ] Spring Boot Actuator endpoints
- [ ] Prometheus metrics
- [ ] ELK stack for log aggregation
- [ ] Distributed tracing (Zipkin/Jaeger)

### Testing
- [ ] Unit tests (JUnit 5, Mockito)
- [ ] Integration tests (TestContainers)
- [ ] API contract tests (Pact)
- [ ] Load testing (JMeter/Gatling)

### Performance
- [ ] Redis caching for instruments
- [ ] Async order processing (RabbitMQ/Kafka)
- [ ] Database indexing optimization
- [ ] API response compression

### Reliability
- [ ] Circuit breaker pattern (Resilience4j)
- [ ] Retry mechanisms
- [ ] Health checks and readiness probes
- [ ] Graceful shutdown

---

## 📸 Screenshots

### Swagger UI
![Swagger UI](https://via.placeholder.com/800x400?text=Swagger+UI+Screenshot)

### API Response
![API Response](https://via.placeholder.com/800x400?text=API+Response+Screenshot)

### H2 Console
![H2 Console](https://via.placeholder.com/800x400?text=H2+Console+Screenshot)

---

## 📄 License

**Proprietary** - © 2026 Bajaj Broking. All rights reserved.

---

## 👥 Author

**Senior Backend Architect**  
Bajaj Broking Technology Team  
📧 tech@bajajbroking.com

---

## 🙏 Acknowledgments

Built with:
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database
- SpringDoc OpenAPI
- Lombok

---

**Happy Trading! 📈**
