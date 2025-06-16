package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory,Long> {

}
