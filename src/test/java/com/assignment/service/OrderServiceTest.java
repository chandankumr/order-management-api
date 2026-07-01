package com.assignment.service;

import com.assignment.domain.CustomerType;
import com.assignment.domain.Order;
import com.assignment.dto.CreateOrderRequest;
import com.assignment.dto.OrderResponse;
import com.assignment.exception.OrderNotFoundException;
import com.assignment.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order(1L, "CUST001", CustomerType.PREMIUM, 
                             new BigDecimal("100.00"), LocalDateTime.now());
    }

    @Test
    void shouldCreateOrder() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST001", CustomerType.PREMIUM, new BigDecimal("100.00")
        );
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        OrderResponse response = orderService.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals("CUST001", response.customerId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldGetOrderById() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act
        OrderResponse response = orderService.getOrderById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> 
            orderService.getOrderById(999L)
        );
    }

    @Test
    void shouldCalculateMonthlyRevenueWithPremiumDiscount() {
        // Arrange
        Order premiumOrder = new Order(1L, "CUST001", CustomerType.PREMIUM, 
                                      new BigDecimal("100.00"), LocalDateTime.now());
        Order standardOrder = new Order(2L, "CUST002", CustomerType.STANDARD, 
                                       new BigDecimal("50.00"), LocalDateTime.now());
        
        when(orderRepository.findAll()).thenReturn(List.of(premiumOrder, standardOrder));

        // Act
        Map<YearMonth, BigDecimal> revenue = orderService.calculateMonthlyRevenue();

        // Assert
        assertNotNull(revenue);
        YearMonth currentMonth = YearMonth.now();
        assertTrue(revenue.containsKey(currentMonth));
        // Premium: 100 * 0.90 = 90, Standard: 50 = 50, Total: 140
        assertEquals(new BigDecimal("140.00"), revenue.get(currentMonth));
    }

    @Test
    void shouldIgnoreNegativeAmountsInRevenueCalculation() {
        // Arrange
        Order validOrder = new Order(1L, "CUST001", CustomerType.STANDARD, 
                                    new BigDecimal("100.00"), LocalDateTime.now());
        Order negativeOrder = new Order(2L, "CUST002", CustomerType.STANDARD, 
                                       new BigDecimal("-50.00"), LocalDateTime.now());
        
        when(orderRepository.findAll()).thenReturn(List.of(validOrder, negativeOrder));

        // Act
        Map<YearMonth, BigDecimal> revenue = orderService.calculateMonthlyRevenue();

        // Assert
        YearMonth currentMonth = YearMonth.now();
        assertEquals(new BigDecimal("100.00"), revenue.get(currentMonth));
    }
}