package com.Ecommerce.demo.requests;

import com.Ecommerce.demo.domain.USER_ROLE;
import lombok.Data;

@Data
public class OtpLoginRequest {
    private String email;
    private String otp;
    private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;
}
