package com.Ecommerce.demo.service;

import com.Ecommerce.demo.domain.AccountStatus;
import com.Ecommerce.demo.exceptions.SellerException;
import com.Ecommerce.demo.model.Seller;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus accountStatus);
    Seller updateSeller(Long id,Seller seller) throws Exception;
    void deleteSeller(Long id) throws Exception;
    Seller verifySeller(String email,String otp) throws Exception;
    Seller updateSellerAccountStatus(Long sellerId,AccountStatus accountStatus) throws Exception;
}
