package com.Ecommerce.demo.model;

import com.Ecommerce.demo.domain.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDetails {
    private String paymentId;
    private String razorPayPaymentLinkId;
    private String razorPayPaymentLinkReferenceId;
    private String razorPayPaymentLinkStatus ;
    private String razorPayPaymentZMSP;
    private PaymentStatus status;
}
