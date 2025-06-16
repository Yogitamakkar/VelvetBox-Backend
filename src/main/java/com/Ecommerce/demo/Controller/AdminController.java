package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.domain.AccountStatus;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

    private final SellerService sellerService;

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long id,
            @PathVariable AccountStatus status
            ) throws Exception {
        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }
}
