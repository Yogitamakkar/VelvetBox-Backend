package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.Cart;
import com.Ecommerce.demo.model.CartItem;
import com.Ecommerce.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepostory extends JpaRepository<CartItem,Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product,String size);
}
