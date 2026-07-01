# Order Management API

## Architecture & Advanced Features
This application is built using Java 17 and Spring Boot 3.x. To ensure production-readiness, the following advanced practices were implemented:
1. **Thread-Safe In-Memory Storage:** Used `ConcurrentHashMap` and `AtomicLong` to ensure the application is thread-safe and can handle concurrent requests without data corruption.
2. **Modern Java Features:** Utilized Java 17 `record` types for immutable DTOs, reducing boilerplate and ensuring thread safety.
3. **Global Exception Handling:** Implemented `@RestControllerAdvice` to provide consistent, structured JSON error responses and proper HTTP status codes (400, 404).
4. **Bean Validation:** Used Jakarta Validation (`@Valid`, `@Positive`) to reject invalid payloads at the controller level before they reach the business logic.
5. **Clean Architecture:** Strict separation of concerns (Controller -> Service -> Repository).