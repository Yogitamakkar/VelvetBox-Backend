package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.model.Wishlist;
import com.Ecommerce.demo.service.ProductService;
import com.Ecommerce.demo.service.UserService;
import com.Ecommerce.demo.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final UserService userService;
    private final WishlistService wishlistService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwt(jwt);
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(user,product);
        return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
    }
}
