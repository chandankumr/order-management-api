package com.assignment.exception;

/**
 * Exception thrown when a requested order cannot be found.
 * 
 * <p>This runtime exception is used to signal that an order lookup
 * failed because the specified order ID does not exist in the system.</p>
 * 
 * @author Chandan
 * @version 1.0
 */
public class OrderNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new OrderNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining why the exception was thrown
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}