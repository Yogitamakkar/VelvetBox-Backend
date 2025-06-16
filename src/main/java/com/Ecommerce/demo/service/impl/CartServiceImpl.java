package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.model.Cart;
import com.Ecommerce.demo.model.CartItem;
import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.repository.CartItemRepostory;
import com.Ecommerce.demo.repository.CartRepository;
import com.Ecommerce.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepostory cartItemRepostory;

    @Override
    public CartItem addCartItem(User user, String size, Product product, int quanity) {
        Cart cart = findUserCart(user);

        CartItem cartItemPresent = cartItemRepostory.findByCartAndProductAndSize(cart, product, size);

        if (cartItemPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setUserId(user.getId());
            cartItem.setQuantity(quanity);
            cartItem.setSize(size);

            // Set MRP and selling price in the cart item
            cartItem.setMrpPrice(quanity * product.getMrpPrice()); // Assuming Product has getMrpprice()
            cartItem.setSellingPrice(quanity * product.getSellingprice());

            // Update cart total prices
            cart.setTotalMrpPrice(cart.getTotalMrpPrice() + cartItem.getMrpPrice());
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() + cartItem.getSellingPrice());

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepostory.save(cartItem);
        } else {
            // If item already exists, update quantity and prices
            int additionalQuantity = quanity;
            int newQuantity = cartItemPresent.getQuantity() + additionalQuantity;

            // Calculate price differences
            int mrpDifference = additionalQuantity * product.getMrpPrice();
            int sellingDifference = additionalQuantity * product.getSellingprice();

            // Update cart item
            cartItemPresent.setQuantity(newQuantity);
            cartItemPresent.setMrpPrice(cartItemPresent.getMrpPrice() + mrpDifference);
            cartItemPresent.setSellingPrice(cartItemPresent.getSellingPrice() + sellingDifference);

            // Update cart totals
            cart.setTotalMrpPrice(cart.getTotalMrpPrice() + mrpDifference);
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() + sellingDifference);

            return cartItemRepostory.save(cartItemPresent);
        }
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalItems(totalItem);
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercent(totalPrice,totalDiscountedPrice));


        // Only calculate discount if MRP is greater than zero 
        if (totalPrice > 0) {
            cart.setDiscount(calculateDiscountPercent(totalPrice, totalDiscountedPrice));
        } else {
            cart.setDiscount(0); // Default to 0% discount if no MRP price
        }

        return cart;
    }

    public int calculateDiscountPercent(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0) {
            return 0; // Return 0% discount instead of throwing exception
        }

        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }
}