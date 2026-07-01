package com.assignment.controller;

import com.assignment.dto.CreateOrderRequest;
import com.assignment.dto.OrderResponse;
import com.assignment.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "Endpoints for creating, retrieving, and analyzing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new order with customer details and amount. Applies validation rules.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., negative amount or missing fields)")
    })
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves a specific order using its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found with the provided ID")
    })
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Unique ID of the order", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    @Operation(summary = "Get orders by month", description = "Retrieves all orders placed in a specific month.")
    public ResponseEntity<List<OrderResponse>> getOrdersByMonth(
            @Parameter(description = "Month in YYYY-MM format", example = "2026-07") @RequestParam String month) {
        YearMonth yearMonth = YearMonth.parse(month); 
        return ResponseEntity.ok(orderService.getOrdersByMonth(yearMonth));
    }

    @GetMapping("/revenue")
    @Operation(summary = "Calculate monthly revenue", description = "Calculates total revenue grouped by month. Applies 10% discount for PREMIUM customers and ignores negative/null amounts.")
    public ResponseEntity<Map<YearMonth, BigDecimal>> getMonthlyRevenue() {
        return ResponseEntity.ok(orderService.calculateMonthlyRevenue());
    }
}