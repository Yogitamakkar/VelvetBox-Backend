package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.User;

public interface UserService  {
    User findUserByJwt(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
