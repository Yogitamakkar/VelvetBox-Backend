package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.model.Wishlist;

public interface WishlistService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
