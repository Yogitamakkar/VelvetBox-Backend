package com.Ecommerce.demo.requests;

import com.Ecommerce.demo.domain.USER_ROLE;
import lombok.Data;

@Data
public class OtpGenerationRequest {
    private String email;
    private USER_ROLE role;
}
