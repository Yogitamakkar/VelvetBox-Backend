package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.domain.OrderStatus;
import com.Ecommerce.demo.model.Order;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.service.OrderService;
import com.Ecommerce.demo.service.SellerReportService;
import com.Ecommerce.demo.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders =  orderService.sellerOrder(seller.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId, @PathVariable OrderStatus orderStatus) throws Exception {
        Order order = orderService.updateOrderStatus(orderId,orderStatus);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
}
