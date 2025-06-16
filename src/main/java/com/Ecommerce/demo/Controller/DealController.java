package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.model.Deal;
import com.Ecommerce.demo.response.ApiResponse;
import com.Ecommerce.demo.service.DealService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeals(@RequestBody Deal deal){
        Deal createdDeal = dealService.createDeal(deal);
        return new ResponseEntity<>(createdDeal, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeals(@RequestBody Deal deal,@PathVariable Long id) throws Exception {
        Deal updatedDeal = dealService.updateDeal(deal, id);
        return new ResponseEntity<>(updatedDeal,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeals(@PathVariable Long id) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse response = new ApiResponse();
        response.setMessage("deal deleted successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
