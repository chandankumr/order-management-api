package com.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

/**
 * Configuration to provide a no-op PlatformTransactionManager.
 * 
 * <p>Since we are using an in-memory repository (ConcurrentHashMap) instead of a real database,
 * Spring does not automatically configure a PlatformTransactionManager. However, to demonstrate
 * proper transactional boundaries (and satisfy the requirement of using @Transactional),
 * we provide this no-op implementation.</p>
 * 
 * <p><strong>Note:</strong> When the application is migrated to a real database (e.g., PostgreSQL), 
 * this configuration class can be safely deleted, and Spring Boot will automatically configure 
 * the appropriate transaction manager (e.g., JpaTransactionManager).</p>
 * 
 * @author Chandan
 * @version 1.0
 */
@Configuration
public class TransactionConfig {

    /**
     * Provides a no-op PlatformTransactionManager for the in-memory repository.
     *
     * @return a dummy transaction manager that performs no actual database commits/rollbacks
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus status) throws TransactionException {
                // No-op for in-memory storage
            }

            @Override
            public void rollback(TransactionStatus status) throws TransactionException {
                // No-op for in-memory storage
            }
        };
    }
}