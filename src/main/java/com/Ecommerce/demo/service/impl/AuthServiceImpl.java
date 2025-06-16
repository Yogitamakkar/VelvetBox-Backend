package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.config.JwtProvider;
import com.Ecommerce.demo.domain.USER_ROLE;
import com.Ecommerce.demo.model.Cart;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.model.VerificationCode;
import com.Ecommerce.demo.repository.CartRepository;
import com.Ecommerce.demo.repository.SellerRepository;
import com.Ecommerce.demo.repository.UserRepository;
import com.Ecommerce.demo.repository.VerificationCodeRepository;
import com.Ecommerce.demo.requests.*;
import com.Ecommerce.demo.response.AuthResponse;
import com.Ecommerce.demo.service.AuthService;
import com.Ecommerce.demo.service.EmailService;
import com.Ecommerce.demo.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customerService;
    private final SellerRepository sellerRepository;

    //otp generated
    @Override
    public void sentLoginOtp(String email,USER_ROLE role) throws Exception {
//        String SIGNIN_PREFIX = "signin_";
//        if (email.startsWith(SIGNIN_PREFIX)){
//            email = email.substring(SIGNIN_PREFIX.length());
//
//            if(role.equals(USER_ROLE.ROLE_SELLER)){
//                Seller seller = sellerRepository.findByEmail(email);
//                if (seller ==  null){
//                    throw new Exception("seller not found by this email..");
//                }
//            }
//            else{
//                User user = userRepository.findByEmail(email);
//                if (user == null){
//                    throw new Exception("user not found by this email");
//                }
//            }
//
//        }
        if(role.equals(USER_ROLE.ROLE_SELLER)){
            Seller seller = sellerRepository.findByEmail(email);
            if(seller == null){
                throw new Exception("Seller not found with this email ");
            }
        }else{
            User user = userRepository.findByEmail(email);
            if(user== null){
                throw new Exception("user not found with this email ");
            }
        }
        VerificationCode existingOtp = verificationCodeRepository.findByEmail(email);
        if (existingOtp != null) {
            verificationCodeRepository.delete(existingOtp);
        }


        String otp = OtpUtils.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);

        String subject = "e-commerce verification code";
        String text = "here is your otp for the login : "+otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);
    }

    //saves the authenticated user into the database
    @Override
    public String createUser(SignUpRequest req) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new Exception("wrong otp ");
        }

        User user = userRepository.findByEmail(req.getEmail());

        if(user == null){
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("8989898989");
            createdUser.setPassword(passwordEncoder.encode(req.getPassword()));

            user = userRepository.save(createdUser);
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication  = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    //sign in the authenticated user and gives the jwt token
    @Override
    public AuthResponse signIn(OtpLoginRequest req) throws Exception {

        String email = req.getEmail();

        if(email.startsWith("seller_")) {
            email = email.substring("seller_".length());
        }
        USER_ROLE role = req.getRole();
        if(role.equals(USER_ROLE.ROLE_SELLER)){
            Seller seller = sellerRepository.findByEmail(email);
            if(seller == null){
                throw new Exception("seller not found");
            }
        }
        else {
            User user = userRepository.findByEmail(email);
            if(user== null) {
                throw new Exception("Invalid user name");
            }
        }
        
        String otp = req.getOtp();

        Authentication authentication =  authenticate(email,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse  authResponse = new AuthResponse();
        authResponse.setMessage("login successful");
        authResponse.setJwt(token);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        return authResponse;
        }

    //this checks the user with password
    @Override
    public AuthResponse logInWithPassword(PasswordLoginRequest req) throws Exception {
        String email = req.getEmail();
        String password = req.getPassword();
        USER_ROLE role = req.getRole();

        if(role.equals(USER_ROLE.ROLE_SELLER)){
            Seller seller = sellerRepository.findByEmail(email);
            if(seller == null){
                throw new Exception("seller not found ");
            }
            if(!passwordEncoder.matches(password,seller.getPassword())){
                throw new BadCredentialsException("password is wrong");
            }
        }else{
            User user = userRepository.findByEmail(req.getEmail());
            if(user== null) {
                throw new BadCredentialsException("Invalid user name");
            }
            if (!passwordEncoder.matches(req.getPassword(),user.getPassword())){
                throw new BadCredentialsException("password did not match");
            }
        }
        //setup authentication
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login successful using the password");
        authResponse.setJwt(token);
        return authResponse;
    }

    //authenticate the user
    private Authentication authenticate(String username, String otp) throws Exception {
        String SellerPrefix = "seller_";
        if(username.startsWith(SellerPrefix)){
            username = username.substring(SellerPrefix.length());
        }
        UserDetails userDetails = customerService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("invalid username or otp");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("wrong otp");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities() );
    }

}

