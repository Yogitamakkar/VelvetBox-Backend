package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.model.*;
import com.Ecommerce.demo.repository.PaymentOrderRepository;
import com.Ecommerce.demo.response.ApiResponse;
import com.Ecommerce.demo.response.PaymentLinkResponse;
import com.Ecommerce.demo.service.*;
import com.razorpay.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final OrderService orderService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    @GetMapping("{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user = userService.findUserByJwt(jwt);
        PaymentLinkResponse paymentLinkResponse;
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder,paymentId,paymentLinkId);
        if (paymentSuccess){
            for (Order  order : paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalSales(report.getTotalSales()+ order.getOrderItem().size());
                sellerReportService.updateSellerReport(report);
            }
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("payment successful");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
