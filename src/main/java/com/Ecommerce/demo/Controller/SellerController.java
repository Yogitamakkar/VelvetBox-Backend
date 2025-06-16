package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.domain.AccountStatus;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.model.SellerReport;
import com.Ecommerce.demo.model.VerificationCode;
import com.Ecommerce.demo.repository.VerificationCodeRepository;
import com.Ecommerce.demo.requests.OtpLoginRequest;
import com.Ecommerce.demo.response.AuthResponse;
import com.Ecommerce.demo.service.AuthService;
import com.Ecommerce.demo.service.EmailService;
import com.Ecommerce.demo.service.SellerReportService;
import com.Ecommerce.demo.service.SellerService;
import com.Ecommerce.demo.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final AuthService authService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final SellerService sellerService;
    private final EmailService emailService;
    private final SellerReportService sellerReportService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody OtpLoginRequest req) throws Exception {
        String otp = req.getOtp();
        String email = req.getEmail();
        req.setEmail("seller_"+email);
        AuthResponse authResponse = authService.signIn(req);
        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("wrong otp..");
        }
        Seller seller = sellerService.verifySeller(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtils.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "e commerce website Email verification code";
        String text = "welcome to our e commerce website this is the your otp for the seller account:";
//

        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(),subject,text);
        return new ResponseEntity<>(seller,HttpStatus.OK);

    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return  new ResponseEntity<>(report,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus accountStatus){
        List<Seller> sellers = sellerService.getAllSellers(accountStatus);
        return ResponseEntity.ok(sellers);
    }
    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
                                               @RequestBody Seller seller) throws Exception {
        Seller existingSeller = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(existingSeller.getId(),seller);

        return ResponseEntity.ok(updatedSeller);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {
        Seller seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
