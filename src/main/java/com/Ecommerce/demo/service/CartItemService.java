package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws Exception;
    void removeItem(Long userId,Long cartItemId) throws Exception;
    CartItem findCartItemById(Long id) throws Exception;
}
