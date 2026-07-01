package com.assignment.service;

import com.assignment.domain.Order;
import com.assignment.dto.CreateOrderRequest;
import com.assignment.dto.MonthlyRevenueResponse;
import com.assignment.dto.OrderResponse;
import com.assignment.enums.CustomerType;
import com.assignment.exception.OrderNotFoundException;
import com.assignment.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Service class for managing order-related business logic.
 * Handles order creation, retrieval, and revenue calculations.
 * 
 * @author Chandan
 * @version 1.0
 */
@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    /**
     * Discount multiplier for PREMIUM customers.
     * Configurable via application.properties (default: 0.90 for 10% discount).
     */
    @Value("${order.discount.premium:0.90}")
    private BigDecimal premiumDiscount;

    /**
     * Constructs an OrderService with the specified repository.
     *
     * @param orderRepository the repository for order data access
     */
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new order with the provided request details.
     *
     * @param request the order creation request containing customer and amount details
     * @return the created order response with generated ID and timestamp
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order(null, request.customerId(), request.customerType(), 
                                request.amount(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id the unique order identifier
     * @return the order response
     * @throws OrderNotFoundException if no order exists with the given ID
     */
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    /**
     * Retrieves all orders for a specific month.
     *
     * @param month the year-month to filter orders
     * @return list of order responses for the specified month
     */
    public List<OrderResponse> getOrdersByMonth(YearMonth month) {
        return orderRepository.findAll().stream()
                .filter(order -> YearMonth.from(order.getOrderDate()).equals(month))
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Calculates monthly revenue grouped by YearMonth.
     * Applies configured discount for PREMIUM customers and ignores null/negative amounts.
     * Results are sorted chronologically using TreeMap.
     *
     * @return list of monthly revenue responses sorted by year-month
     */
    public List<MonthlyRevenueResponse> calculateMonthlyRevenue() {
        TreeMap<YearMonth, BigDecimal> revenueMap = orderRepository.findAll().stream()
                .filter(order -> order.getAmount() != null && order.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.groupingBy(
                        order -> YearMonth.from(order.getOrderDate()),
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, this::calculateFinalAmount, BigDecimal::add)
                ));
        
        return revenueMap.entrySet().stream()
                .map(entry -> new MonthlyRevenueResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    /**
     * Calculates the final amount for an order after applying applicable discounts.
     *
     * @param order the order to calculate
     * @return the final amount after discount application
     */
    private BigDecimal calculateFinalAmount(Order order) {
        BigDecimal amount = order.getAmount();
        if (order.getCustomerType() == CustomerType.PREMIUM) {
            return amount.multiply(premiumDiscount).setScale(2, RoundingMode.HALF_UP);
        }
        return amount;
    }

    /**
     * Maps an Order domain object to an OrderResponse DTO.
     *
     * @param order the order domain object
     * @return the order response DTO
     */
    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getCustomerType(), 
                                 order.getAmount(), order.getOrderDate());
    }
}