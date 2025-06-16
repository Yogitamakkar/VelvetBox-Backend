package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.Cart;
import com.Ecommerce.demo.model.CartItem;
import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.User;

public interface CartService {
    public CartItem addCartItem(User user , String size , Product product, int quanity);
    public Cart findUserCart(User user) ;

}
