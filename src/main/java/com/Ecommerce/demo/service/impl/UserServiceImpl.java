package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.config.JwtProvider;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.repository.UserRepository;
import com.Ecommerce.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    
    @Override
    public User findUserByJwt(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = this.findUserByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new Exception("user not found..");
        }
        return user;
    }
}
