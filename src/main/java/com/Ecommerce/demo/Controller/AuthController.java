package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.domain.USER_ROLE;
import com.Ecommerce.demo.repository.UserRepository;
import com.Ecommerce.demo.requests.OtpGenerationRequest;
import com.Ecommerce.demo.requests.OtpLoginRequest;
import com.Ecommerce.demo.requests.PasswordLoginRequest;
import com.Ecommerce.demo.response.ApiResponse;
import com.Ecommerce.demo.response.AuthResponse;
import com.Ecommerce.demo.requests.SignUpRequest;
import com.Ecommerce.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req) throws Exception {
        String jwt = authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        res.setMessage("registration successful");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody OtpGenerationRequest req) throws Exception {
        authService.sentLoginOtp(req.getEmail(),req.getRole(),req.getPurpose());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("otp sent successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login-signin-password")
    public ResponseEntity<AuthResponse> logInWithPassword(@RequestBody PasswordLoginRequest req) throws Exception {
        AuthResponse response = authService.logInWithPassword(req);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signin-otp")
    public ResponseEntity<AuthResponse> logInWithOtp(@RequestBody OtpLoginRequest req) throws Exception {
        AuthResponse authResponse = authService.signIn(req);
        return ResponseEntity.ok(authResponse);
    }
}
