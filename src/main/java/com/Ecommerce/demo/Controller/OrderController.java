package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.domain.PaymentMethod;
import com.Ecommerce.demo.model.*;
import com.Ecommerce.demo.repository.PaymentOrderRepository;
import com.Ecommerce.demo.response.PaymentLinkResponse;
import com.Ecommerce.demo.service.*;
import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestBody Address ShippingAddress , @RequestParam PaymentMethod paymentMethod , @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwt(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user,ShippingAddress,cart);

        PaymentOrder paymentOrder = paymentService.createOrder(user,orders);

        PaymentLinkResponse response = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZOR_PAY)){
            PaymentLink payment = paymentService.createRazorPayPaymentLink(user,paymentOrder.getAmount(),paymentOrder.getId());
            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            response.setPaymentLinkId(paymentUrlId);

            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        }else{
            String paymentUrl = paymentService.createStripePaymentLink(user,paymentOrder.getAmount(),paymentOrder.getId());
            response.setPaymentLinkUrl(paymentUrl);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Order orders = orderService.findOrderById(orderId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> findOrderItemById(@PathVariable Long orderItemId,@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem,HttpStatus.ACCEPTED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        List<Order> orders = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }


    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId,@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Order order = orderService.cancelOrder(user, orderId);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);

        report.setCancelledOrders(report.getCancelledOrders()+1);
        report.setTotalRefunds(report.getTotalRefunds()+ order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }
}
