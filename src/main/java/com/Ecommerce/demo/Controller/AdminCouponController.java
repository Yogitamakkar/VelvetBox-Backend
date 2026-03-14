package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.model.Cart;
import com.Ecommerce.demo.model.Coupon;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.service.CouponService;
import com.Ecommerce.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminCouponController {

    private final UserService userService;
    private final CouponService couponService;
    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String code,
            @RequestParam double orderValue,
            @RequestParam String apply
    ) throws Exception {

        User user = userService.findUserByJwt(jwt);
        Cart cart;
        if (apply.equals("true")){
            cart = couponService.applyCoupon(code,orderValue,user);
        }
        else{
            cart = couponService.removeCoupon(code,user);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("coupon deleted successfully");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        List<Coupon> coupons = couponService.findAllCoupons();
        return ResponseEntity.ok(coupons);
    }
}
