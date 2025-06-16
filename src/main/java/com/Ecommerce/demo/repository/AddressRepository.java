package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
