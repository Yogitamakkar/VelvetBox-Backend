package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findBySellerID(Long sellerId);
}
