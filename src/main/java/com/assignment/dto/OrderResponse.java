package com.assignment.dto;

import com.assignment.domain.CustomerType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String customerId,
        CustomerType customerType,
        BigDecimal amount,
        LocalDateTime orderDate
) {}