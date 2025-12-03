package com.realtyx.controller;

import com.realtyx.model.Review;
import com.realtyx.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/target/{targetId}")
    public List<Review> getReviewsByTarget(@PathVariable Long targetId, @RequestParam String targetType) {
        return reviewService.getReviewsByTarget(targetId, targetType);
    }

    @GetMapping("/reviewer/{reviewerId}")
    public List<Review> getReviewsByReviewer(@PathVariable Long reviewerId, @RequestParam String reviewerType) {
        return reviewService.getReviewsByReviewer(reviewerId, reviewerType);
    }

    @GetMapping("/average/{targetId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long targetId, @RequestParam String targetType) {
        Double avgRating = reviewService.getAverageRating(targetId, targetType);
        return ResponseEntity.ok(avgRating);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        review.setId(id);
        return ResponseEntity.ok(reviewService.saveReview(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        boolean deleted = reviewService.deleteReview(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
