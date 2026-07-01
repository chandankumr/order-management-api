package com.assignment.osgi.consumer;

import com.assignment.osgi.DiscountService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.math.BigDecimal;
import java.util.List;

@Component(immediate = true)
public class OrderProcessor {
    
    // Injects all available DiscountService implementations dynamically
    @Reference(policy = org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC, 
               cardinality = org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE)
    private volatile List<DiscountService> discountServices;

    public void processOrder(BigDecimal amount) {
        System.out.println("Processing order with amount: " + amount);
        for (DiscountService service : discountServices) {
            BigDecimal discounted = service.applyDiscount(amount);
            System.out.println("Applied " + service.getProviderName() + ": Final amount = " + discounted);
        }
    }
}