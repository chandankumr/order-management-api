package com.assignment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Long id;
    private String customerId;
    private CustomerType customerType;
    private BigDecimal amount;
    private LocalDateTime orderDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    // Constructors, Getters, Setters omitted for brevity (use Lombok or generate them)
    public Order(Long id, String customerId, CustomerType customerType, BigDecimal amount, LocalDateTime orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.customerType = customerType;
        this.amount = amount;
        this.orderDate = orderDate;
    }
}