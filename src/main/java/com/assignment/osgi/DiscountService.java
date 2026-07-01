package com.assignment.osgi;
import java.math.BigDecimal;

public interface DiscountService {
    BigDecimal applyDiscount(BigDecimal amount);
    String getProviderName();
}