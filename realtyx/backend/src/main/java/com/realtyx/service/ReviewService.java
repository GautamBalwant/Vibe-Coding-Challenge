package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtyx.model.Review;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private static final String DATA_FILE = "../data/reviews.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Review> getAllReviews() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Review>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Review> getReviewById(Long id) {
        return getAllReviews().stream()
                .filter(review -> review.getId().equals(id))
                .findFirst();
    }

    public List<Review> getReviewsByTarget(Long targetId, String targetType) {
        return getAllReviews().stream()
                .filter(review -> review.getTargetId().equals(targetId) && 
                                 review.getTargetType().equalsIgnoreCase(targetType))
                .toList();
    }

    public List<Review> getReviewsByReviewer(Long reviewerId, String reviewerType) {
        return getAllReviews().stream()
                .filter(review -> review.getReviewerId().equals(reviewerId) && 
                                 review.getReviewerType().equalsIgnoreCase(reviewerType))
                .toList();
    }

    public Review saveReview(Review review) {
        List<Review> reviews = getAllReviews();
        
        // Generate new ID if not present
        if (review.getId() == null) {
            long maxId = reviews.stream()
                    .mapToLong(Review::getId)
                    .max()
                    .orElse(0L);
            review.setId(maxId + 1);
        }
        
        // Update existing review or add new one
        reviews.removeIf(r -> r.getId().equals(review.getId()));
        reviews.add(review);
        
        saveToFile(reviews);
        return review;
    }

    public boolean deleteReview(Long id) {
        List<Review> reviews = getAllReviews();
        boolean removed = reviews.removeIf(review -> review.getId().equals(id));
        if (removed) {
            saveToFile(reviews);
        }
        return removed;
    }

    public Double getAverageRating(Long targetId, String targetType) {
        List<Review> targetReviews = getReviewsByTarget(targetId, targetType);
        if (targetReviews.isEmpty()) {
            return 0.0;
        }
        return targetReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    private void saveToFile(List<Review> reviews) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), reviews);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
