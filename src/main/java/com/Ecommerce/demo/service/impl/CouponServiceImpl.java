package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.model.Cart;
import com.Ecommerce.demo.model.Coupon;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.repository.CartRepository;
import com.Ecommerce.demo.repository.CouponRepository;
import com.Ecommerce.demo.repository.UserRepository;
import com.Ecommerce.demo.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());
        if (coupon == null){
            throw new Exception("coupon is null");
        }
        if (user.getUsedCoupons().contains(coupon)){
            throw new Exception("coupon already used");
        }
        if(orderValue < coupon.getMinimumOrderValue()){
            throw new Exception("valid for minimum order value " + coupon.getMinimumOrderValue());
        }
        if (coupon.isActive()) {
            LocalDate now = LocalDate.now();
            if (now.isAfter(coupon.getValidityStartDate()) && now.isBefore(coupon.getValidityEndDate())) {
                user.getUsedCoupons().add(coupon);
                userRepository.save(user);
                double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
                cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountedPrice);
                cart.setCouponCode(code);
                cartRepository.save(cart);
                return cart;
            }
        }
        throw new Exception("coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new Exception("coupon value is null");
        }
        Cart cart = cartRepository.findByUserId(user.getId());
        double discountedPrice = (cart.getTotalSellingPrice()* coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountedPrice);
        cart.setCouponCode(null);
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(()->new Exception("coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepository.deleteById(id);
    }
}
