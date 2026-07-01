package com.assignment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.assignment.enums.CustomerType;

public record OrderResponse(
        Long id,
        String customerId,
        CustomerType customerType,
        BigDecimal amount,
        LocalDateTime orderDate
) {}