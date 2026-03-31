package com.creditflow.backend.repository;

import com.creditflow.backend.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository marks this as a Spring-managed data access component
// Spring sees this and knows it should be available for @Autowired injection
@Repository

// JpaRepository<Merchant, Long> means:
// - We're working with the Merchant entity
// - The primary key type is Long (matches our "private Long id" field)
// By extending this interface, Spring automatically gives you these methods for FREE:
// save(merchant)        — insert or update
// findById(id)          — get one merchant by ID
// findAll()             — get all merchants
// deleteById(id)        — delete one merchant
// count()               — how many merchants exist
// You write ZERO implementation code. Spring generates it all at runtime.
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    // You can declare custom queries just by naming methods a certain way
    // Spring reads the method name and generates the SQL automatically
    // This one becomes: SELECT * FROM merchants WHERE email = ?
    boolean existsByEmail(String email);

}