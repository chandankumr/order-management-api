package com.assignment.controller;

import com.assignment.domain.CustomerType;
import com.assignment.dto.CreateOrderRequest;
import com.assignment.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
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
    void shouldGetMonthlyRevenue() throws Exception {
        // Arrange
        YearMonth currentMonth = YearMonth.now();
        when(orderService.calculateMonthlyRevenue())
            .thenReturn(Map.of(currentMonth, new BigDecimal("1000.00")));

        // Act & Assert
        mockMvc.perform(get("/orders/revenue"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isMap());
    }
}