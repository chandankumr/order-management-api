package com.assignment.repository;

import com.assignment.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of OrderRepository using thread-safe data structures.
 * Uses ConcurrentHashMap for storage and AtomicLong for ID generation.
 * Suitable for development, testing, and demo purposes.
 * 
 * <p><strong>Note:</strong> Data is not persisted and will be lost on application restart.</p>
 * 
 * @author Chandan
 * @version 1.0
 */
@Repository
public class InMemoryOrderRepository implements OrderRepository {
    
    /** Thread-safe storage for orders */
    private final ConcurrentHashMap<Long, Order> store = new ConcurrentHashMap<>();
    
    /** Thread-safe ID generator starting from 1 */
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * {@inheritDoc}
     * 
     * <p>Generates a new ID using AtomicLong if the order has no ID,
     * then stores the order in the ConcurrentHashMap.</p>
     */
    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
            order = new Order(idGenerator.getAndIncrement(), order.getCustomerId(), 
                              order.getCustomerType(), order.getAmount(), order.getOrderDate());
        }
        store.put(order.getId(), order);
        return order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Returns an immutable copy of all orders to prevent external modification.</p>
     */
    @Override
    public List<Order> findAll() {
        return List.copyOf(store.values());
    }
}