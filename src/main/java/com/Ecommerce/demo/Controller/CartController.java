package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.model.Cart;
import com.Ecommerce.demo.model.CartItem;
import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.requests.AddItemRequest;
import com.Ecommerce.demo.response.ApiResponse;
import com.Ecommerce.demo.service.CartItemService;
import com.Ecommerce.demo.service.CartService;
import com.Ecommerce.demo.service.ProductService;
import com.Ecommerce.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestHeader("Authorization") String jwt,@RequestBody AddItemRequest request) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Product product = productService.findProductById(request.getProductId());
        CartItem item = cartService.addCartItem(user, request.getSize(), product,request.getQuantity());
        return new ResponseEntity<>(item,HttpStatus.OK);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId,@RequestHeader("Authorization") String jwt,@RequestBody CartItem cartItem) throws Exception {
        User user = userService.findUserByJwt(jwt);
        CartItem updatedCartItem = null;
        if(cartItem.getQuantity() > 0){
            updatedCartItem = cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);
        }

        return new ResponseEntity<>(updatedCartItem,HttpStatus.OK);
    }
    @DeleteMapping("/item/{id}")
    public ResponseEntity<ApiResponse> removeCart(@PathVariable Long id,@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        cartItemService.removeItem(user.getId(), id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("item removed from the cart");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
