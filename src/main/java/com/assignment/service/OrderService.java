package com.assignment.service;

import com.assignment.domain.CustomerType;
import com.assignment.domain.Order;
import com.assignment.dto.CreateOrderRequest;
import com.assignment.dto.OrderResponse;
import com.assignment.exception.OrderNotFoundException;
import com.assignment.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private static final BigDecimal PREMIUM_DISCOUNT = new BigDecimal("0.90"); // 10% discount

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order(null, request.customerId(), request.customerType(), 
                                request.amount(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    public List<OrderResponse> getOrdersByMonth(YearMonth month) {
        return orderRepository.findAll().stream()
                .filter(order -> YearMonth.from(order.getOrderDate()).equals(month))
                .map(this::mapToResponse)
                .toList();
    }

    // --- PART 1: REVENUE CALCULATION ---
    public Map<YearMonth, BigDecimal> calculateMonthlyRevenue() {
        return orderRepository.findAll().stream()
                // Ignore null or negative amounts
                .filter(order -> order.getAmount() != null && order.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.groupingBy(
                        order -> YearMonth.from(order.getOrderDate()),
                        Collectors.reducing(BigDecimal.ZERO, this::calculateFinalAmount, BigDecimal::add)
                ));
    }

    private BigDecimal calculateFinalAmount(Order order) {
        BigDecimal amount = order.getAmount();
        if (order.getCustomerType() == CustomerType.PREMIUM) {
            return amount.multiply(PREMIUM_DISCOUNT).setScale(2, RoundingMode.HALF_UP);
        }
        return amount;
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getCustomerType(), 
                                 order.getAmount(), order.getOrderDate());
    }
}