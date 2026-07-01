<!-- # Order Management API

## Architecture & Advanced Features
This application is built using Java 17 and Spring Boot 3.x. To ensure production-readiness, the following advanced practices were implemented:
1. **Thread-Safe In-Memory Storage:** Used `ConcurrentHashMap` and `AtomicLong` to ensure the application is thread-safe and can handle concurrent requests without data corruption.
2. **Modern Java Features:** Utilized Java 17 `record` types for immutable DTOs, reducing boilerplate and ensuring thread safety.
3. **Global Exception Handling:** Implemented `@RestControllerAdvice` to provide consistent, structured JSON error responses and proper HTTP status codes (400, 404).
4. **Bean Validation:** Used Jakarta Validation (`@Valid`, `@Positive`) to reject invalid payloads at the controller level before they reach the business logic.
5. **Clean Architecture:** Strict separation of concerns (Controller -> Service -> Repository). -->

# Order Management API

A production-ready, thread-safe RESTful API built with **Java 17** and **Spring Boot 3.x**. This application manages orders, calculates monthly revenue with customer-specific discounts, and demonstrates advanced Java features, centralized error handling, and clean architecture principles.

## 🏗️ System Architecture

Below is the architectural flow of the application, demonstrating the strict separation of concerns, validation layers, and thread-safe data handling.

```mermaid
graph TD
    Client[HTTP Client: cURL / Postman] -->|REST Requests| Controller[OrderController]
    Controller -->|@Valid Bean Validation| Service[OrderService]
    Controller -->|Exceptions| ExceptionHandler[GlobalExceptionHandler]
    Service -->|Business Logic & Revenue Calc| Repository[OrderRepository Interface]
    Repository -.->|Implementation| InMemoryRepo[InMemoryOrderRepository]
    InMemoryRepo -->|Thread-Safe Storage| Map[(ConcurrentHashMap)]
    InMemoryRepo -->|Thread-Safe ID Gen| Atomic[AtomicLong]

## How to Run
1. Ensure you have Java 17+ and Maven installed.
2. Run the application:
   ```bash
   mvn spring-boot:run