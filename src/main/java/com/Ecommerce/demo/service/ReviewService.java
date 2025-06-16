package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.Review;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.requests.CreateReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest req, User user, Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId,String reviewText,double reviewRating,Long userId) throws Exception;
    void deleteReview(Long reviewId,Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;
}
