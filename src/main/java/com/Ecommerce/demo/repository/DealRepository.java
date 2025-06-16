package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository  extends JpaRepository<Deal,Long> {
    
}
