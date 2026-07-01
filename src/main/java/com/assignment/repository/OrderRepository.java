package com.assignment.repository;

import com.assignment.domain.Order;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for order data access operations.
 * Defines the contract for order persistence and retrieval.
 * 
 * @author Chandan
 * @version 1.0
 */
public interface OrderRepository {
    
    /**
     * Saves an order to the data store.
     * If the order has no ID, a new ID will be generated.
     *
     * @param order the order to save
     * @return the saved order with generated ID (if new)
     */
    Order save(Order order);

    /**
     * Finds an order by its unique identifier.
     *
     * @param id the order ID to search for
     * @return an Optional containing the order if found, or empty if not found
     */
    Optional<Order> findById(Long id);

    /**
     * Retrieves all orders from the data store.
     *
     * @return an immutable list of all orders
     */
    List<Order> findAll();
}