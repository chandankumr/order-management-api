package com.assignment.service;

import com.assignment.domain.Order;
import com.assignment.dto.CreateOrderRequest;
import com.assignment.dto.MonthlyRevenueResponse;
import com.assignment.dto.OrderResponse;
import com.assignment.enums.CustomerType;
import com.assignment.exception.OrderNotFoundException;
import com.assignment.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
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
        
        // FIX: Mockito does not process Spring's @Value annotations.
        // We must manually inject the value for the unit test to prevent NullPointerException.
        ReflectionTestUtils.setField(orderService, "premiumDiscount", new BigDecimal("0.90"));
    }

    @Test
    @DisplayName("Should create order successfully")
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
    @DisplayName("Should get order by ID")
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
    @DisplayName("Should throw exception when order not found")
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> 
            orderService.getOrderById(999L)
        );
    }

    @Test
    @DisplayName("Should calculate monthly revenue with premium discount and return sorted list")
    void shouldCalculateMonthlyRevenueWithPremiumDiscount() {
        // Arrange
        Order premiumOrder = new Order(1L, "CUST001", CustomerType.PREMIUM, 
                                      new BigDecimal("100.00"), LocalDateTime.now());
        Order standardOrder = new Order(2L, "CUST002", CustomerType.STANDARD, 
                                       new BigDecimal("50.00"), LocalDateTime.now());
        
        when(orderRepository.findAll()).thenReturn(List.of(premiumOrder, standardOrder));

        // Act
        List<MonthlyRevenueResponse> revenue = orderService.calculateMonthlyRevenue();

        // Assert
        assertNotNull(revenue);
        assertFalse(revenue.isEmpty());
        
        // Premium: 100 * 0.90 = 90, Standard: 50 = 50, Total: 140
        MonthlyRevenueResponse currentMonthRevenue = revenue.stream()
            .filter(r -> r.yearMonth().equals(YearMonth.now()))
            .findFirst()
            .orElseThrow();
            
        assertEquals(new BigDecimal("140.00"), currentMonthRevenue.revenue());
    }

    @Test
    @DisplayName("Should ignore negative amounts in revenue calculation")
    void shouldIgnoreNegativeAmountsInRevenueCalculation() {
        // Arrange
        Order validOrder = new Order(1L, "CUST001", CustomerType.STANDARD, 
                                    new BigDecimal("100.00"), LocalDateTime.now());
        Order negativeOrder = new Order(2L, "CUST002", CustomerType.STANDARD, 
                                       new BigDecimal("-50.00"), LocalDateTime.now());
        
        when(orderRepository.findAll()).thenReturn(List.of(validOrder, negativeOrder));

        // Act
        List<MonthlyRevenueResponse> revenue = orderService.calculateMonthlyRevenue();

        // Assert
        MonthlyRevenueResponse currentMonthRevenue = revenue.stream()
            .filter(r -> r.yearMonth().equals(YearMonth.now()))
            .findFirst()
            .orElseThrow();
            
        assertEquals(new BigDecimal("100.00"), currentMonthRevenue.revenue());
    }
}