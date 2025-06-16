package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.Review;
import com.Ecommerce.demo.model.User;
import com.Ecommerce.demo.repository.ReviewRepository;
import com.Ecommerce.demo.requests.CreateReviewRequest;
import com.Ecommerce.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProduct(product);
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }


    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double reviewRating, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if (review.getUser().getId().equals(userId)){
            review.setReviewText(reviewText);
            review.setRating(reviewRating);
            return reviewRepository.save(review);
        }
        throw new Exception("you cant update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(!review.getUser().getId().equals(userId)){
            throw new Exception("you cant delete this review");
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(()->new Exception());
    }
}
