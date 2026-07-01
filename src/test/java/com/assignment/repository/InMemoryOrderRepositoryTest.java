package com.assignment.repository;

import com.assignment.domain.Order;
import com.assignment.enums.CustomerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InMemoryOrderRepository.
 * Tests thread-safe storage and ID generation.
 */
class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    @Test
    @DisplayName("Should save order and generate ID when ID is null")
    void shouldSaveOrderAndGenerateId() {
        // Arrange
        Order order = new Order(null, "CUST001", CustomerType.STANDARD, 
                               new BigDecimal("100.00"), LocalDateTime.now());

        // Act
        Order savedOrder = repository.save(order);

        // Assert
        assertNotNull(savedOrder.getId());
        assertEquals("CUST001", savedOrder.getCustomerId());
        assertEquals(new BigDecimal("100.00"), savedOrder.getAmount());
    }

    @Test
    @DisplayName("Should find order by ID")
    void shouldFindOrderById() {
        // Arrange
        Order order = new Order(null, "CUST001", CustomerType.STANDARD, 
                               new BigDecimal("100.00"), LocalDateTime.now());
        Order savedOrder = repository.save(order);

        // Act
        Optional<Order> foundOrder = repository.findById(savedOrder.getId());

        // Assert
        assertTrue(foundOrder.isPresent());
        assertEquals(savedOrder.getId(), foundOrder.get().getId());
    }

    @Test
    @DisplayName("Should return empty Optional when order not found")
    void shouldReturnEmptyWhenOrderNotFound() {
        // Act
        Optional<Order> foundOrder = repository.findById(999L);

        // Assert
        assertTrue(foundOrder.isEmpty());
    }

    @Test
    @DisplayName("Should return all orders")
    void shouldReturnAllOrders() {
        // Arrange
        repository.save(new Order(null, "CUST001", CustomerType.STANDARD, 
                                 new BigDecimal("100.00"), LocalDateTime.now()));
        repository.save(new Order(null, "CUST002", CustomerType.PREMIUM, 
                                 new BigDecimal("200.00"), LocalDateTime.now()));

        // Act
        var orders = repository.findAll();

        // Assert
        assertEquals(2, orders.size());
    }

    @Test
    @DisplayName("Should return immutable list from findAll")
    void shouldReturnImmutableList() {
        // Arrange
        repository.save(new Order(null, "CUST001", CustomerType.STANDARD, 
                                 new BigDecimal("100.00"), LocalDateTime.now()));

        // Act
        var orders = repository.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> orders.add(null));
    }

    @Test
    @DisplayName("Should generate unique IDs for multiple orders")
    void shouldGenerateUniqueIds() {
        // Arrange & Act
        Order order1 = repository.save(new Order(null, "CUST001", CustomerType.STANDARD, 
                                                new BigDecimal("100.00"), LocalDateTime.now()));
        Order order2 = repository.save(new Order(null, "CUST002", CustomerType.PREMIUM, 
                                                new BigDecimal("200.00"), LocalDateTime.now()));

        // Assert
        assertNotEquals(order1.getId(), order2.getId());
    }
}