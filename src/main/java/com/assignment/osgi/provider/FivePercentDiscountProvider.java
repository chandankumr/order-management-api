package com.assignment.osgi.provider;

import com.assignment.osgi.DiscountService;
import org.osgi.service.component.annotations.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component(
    service = DiscountService.class,
    property = {"discountType=5%", "provider.name=FivePercentProvider"}
)
public class FivePercentDiscountProvider implements DiscountService {
    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.95")).setScale(2, RoundingMode.HALF_UP);
    }
    @Override
    public String getProviderName() { return "5% Discount Provider"; }
}