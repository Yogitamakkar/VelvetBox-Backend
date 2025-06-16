package com.Ecommerce.demo.service;

import com.Ecommerce.demo.domain.USER_ROLE;
import com.Ecommerce.demo.requests.OtpLoginRequest;
import com.Ecommerce.demo.requests.PasswordLoginRequest;
import com.Ecommerce.demo.response.AuthResponse;
import com.Ecommerce.demo.requests.SignUpRequest;

public interface AuthService {
    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignUpRequest req) throws Exception;
    AuthResponse signIn(OtpLoginRequest req) throws Exception;
    AuthResponse logInWithPassword(PasswordLoginRequest req) throws Exception;
}
