package com.assignment.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * Response DTO for monthly revenue data.
 * Contains the year-month period and the total revenue for that period.
 * 
 * @param yearMonth the year and month for this revenue calculation
 * @param revenue the total revenue amount for the specified period
 * 
 * @author Chandan
 * @version 1.0
 */
public record MonthlyRevenueResponse(
    YearMonth yearMonth,
    BigDecimal revenue
) {}