package com.assignment.controller;

import com.assignment.dto.CreateOrderRequest;
import com.assignment.dto.MonthlyRevenueResponse;
import com.assignment.enums.CustomerType;
import com.assignment.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// UPDATED: Using the new MockitoBean for Spring Boot 3.4+ to remove deprecation warnings
import org.springframework.test.context.bean.override.mockito.MockitoBean; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // UPDATED: Changed from @MockBean to @MockitoBean
    @MockitoBean
    private OrderService orderService;

    @Test
    @DisplayName("Should create order and return 201 status")
    void shouldCreateOrder() throws Exception {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST001", CustomerType.PREMIUM, new BigDecimal("100.00")
        );

        // Act & Assert
        mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
        
        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    @DisplayName("Should return 400 for invalid request")
    void shouldReturnBadRequestForInvalidRequest() throws Exception {
        // Arrange
        String invalidRequest = """
            {
                "customerId": "",
                "customerType": "PREMIUM",
                "amount": -100
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidRequest))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get orders by month")
    void shouldGetOrdersByMonth() throws Exception {
        // Arrange
        YearMonth currentMonth = YearMonth.now();
        when(orderService.getOrdersByMonth(currentMonth))
            .thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/orders")
            .param("month", currentMonth.toString()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should get monthly revenue as a list of responses")
    void shouldGetMonthlyRevenue() throws Exception {
        // Arrange
        YearMonth currentMonth = YearMonth.now();
        // UPDATED: Returning the new List<MonthlyRevenueResponse> instead of Map
        when(orderService.calculateMonthlyRevenue())
            .thenReturn(List.of(new MonthlyRevenueResponse(currentMonth, new BigDecimal("1000.00"))));

        // Act & Assert
        mockMvc.perform(get("/orders/revenue"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}