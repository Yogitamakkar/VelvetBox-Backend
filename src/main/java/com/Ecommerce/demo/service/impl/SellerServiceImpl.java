package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.config.JwtProvider;
import com.Ecommerce.demo.domain.AccountStatus;
import com.Ecommerce.demo.domain.USER_ROLE;
import com.Ecommerce.demo.exceptions.SellerException;
import com.Ecommerce.demo.model.Address;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.repository.AddressRepository;
import com.Ecommerce.demo.repository.SellerRepository;
import com.Ecommerce.demo.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String seller = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(seller);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExist != null){
            throw new Exception("seller already exists..");
        }
        Address address = addressRepository.save(seller.getPickUpAddress());

        Seller newSeller = new Seller();

        newSeller.setEmail(seller.getEmail());
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setMobile(seller.getMobile());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        newSeller.setPickUpAddress(address);
        newSeller.setRole(USER_ROLE.ROLE_SELLER);

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id).orElseThrow(()-> new SellerException("user not found with this id"));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null){
            throw  new Exception("seller not found");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus accountStatus) {
        return sellerRepository.findByAccountStatus(accountStatus);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller = this.getSellerById(id);

        if (seller.getSellerName() != null){
            existingSeller.setSellerName(seller.getSellerName());
        }
        if(seller.getMobile() != null){
            existingSeller.setMobile(seller.getMobile());
        }
        if(seller.getEmail() != null){
            existingSeller.setEmail(seller.getEmail());
        }
        if (seller.getBusinessDetails() != null
            && seller.getBusinessDetails().getBusinessName() != null
        ){
            existingSeller.getBusinessDetails().setBusinessName(
                    seller.getBusinessDetails().getBusinessName()
            );
        }
        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountNumber() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountHolderName() != null
        ){
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
        }
        if(seller.getPickUpAddress() != null
            && seller.getPickUpAddress().getAddress() != null
            && seller.getPickUpAddress().getState() != null
            && seller.getPickUpAddress().getCity() != null
            && seller.getPickUpAddress().getMobile() != null
        ){
            existingSeller.getPickUpAddress().setAddress(seller.getPickUpAddress().getAddress());
            existingSeller.getPickUpAddress().setMobile(seller.getPickUpAddress().getMobile());
            existingSeller.getPickUpAddress().setCity(seller.getPickUpAddress().getCity());
            existingSeller.getPickUpAddress().setState(seller.getPickUpAddress().getState());
            existingSeller.getPickUpAddress().setLocality(seller.getPickUpAddress().getLocality());
            existingSeller.getPickUpAddress().setPincode(seller.getPickUpAddress().getPincode());
        }

        return sellerRepository.save(seller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = this.getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifySeller(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(accountStatus);
        return sellerRepository.save(seller);
    }
}
