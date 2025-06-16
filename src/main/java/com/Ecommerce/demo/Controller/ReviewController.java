package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.Review;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.requests.CreateReviewRequest;
import com.Ecommerce.demo.response.ApiResponse;
import com.Ecommerce.demo.service.ProductService;
import com.Ecommerce.demo.service.ReviewService;
import com.Ecommerce.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId){
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> createReview(@PathVariable Long productId, @RequestHeader("Authorization") String jwt,@RequestBody CreateReviewRequest request) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Product product = productService.findProductById(productId);

        Review review = reviewService.createReview(
                request,
                user,
                product
        );
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> udpateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Review review = reviewService.updateReview(
                reviewId,
                request.getReviewText(),
                request.getReviewRating(),
                user.getId()
        );
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@RequestHeader("Authorization") String jwt,@PathVariable Long reviewId) throws Exception {
        User user = userService.findUserByJwt(jwt);
        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("review deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }

}
