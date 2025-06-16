package com.Ecommerce.demo.requests;

import com.Ecommerce.demo.domain.USER_ROLE;
import lombok.Data;

@Data
public class PasswordLoginRequest {
    private String email;
    private String password;
    private USER_ROLE role;
}
