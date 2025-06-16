package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.model.CartItem;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.repository.CartItemRepostory;
import com.Ecommerce.demo.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepostory cartItemRepostory;
    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem cartItemExisting = findCartItemById(id);
        User cartItemUser = cartItemExisting.getCart().getUser();
        if (cartItemUser.getId().equals(userId)){
            cartItemExisting.setQuantity(cartItem.getQuantity());
            cartItemExisting.setMrpPrice(cartItemExisting.getQuantity()*cartItemExisting.getProduct().getMrpPrice());
            cartItemExisting.setSellingPrice(cartItemExisting.getQuantity()*cartItemExisting.getProduct().getSellingprice());
            return cartItemRepostory.save(cartItemExisting);
        }
        throw new Exception("you cant update this cart item");
    }

    @Override
    public void removeItem(Long userId, Long cartItemId) throws Exception {
        CartItem cartItemExisting = findCartItemById(cartItemId);
        if(cartItemExisting != null){
            cartItemRepostory.delete(cartItemExisting);
        }
        else throw new Exception("this item doesnt exist");
    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {
        return cartItemRepostory.findById(id).orElseThrow(()->new Exception("item with this id doesnt exist"+ id));
    }

}
