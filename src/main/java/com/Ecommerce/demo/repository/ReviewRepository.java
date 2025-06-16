package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review>  findByProductId(Long productId);
}
