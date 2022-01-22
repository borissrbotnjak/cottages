package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Complaint;
import com.isa.cottages.Model.Review;
import com.isa.cottages.Repository.ReviewRepository;
import com.isa.cottages.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> findAll() {
        return this.reviewRepository.findAll();
    }

    @Override
    public Review findById(Long id) throws Exception {
        return this.reviewRepository.getById(id);
    }

    @Override
    public Review save(Review review) {
        return this.reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        Review forUpdate = this.reviewRepository.getById(review.getId());

        forUpdate.setInstructor(review.getInstructor());
        forUpdate.setBoat(review.getBoat());
        forUpdate.setCottage(review.getCottage());
        forUpdate.setClient(review.getClient());
        forUpdate.setReviewText(review.getReviewText());
        forUpdate.setRating(review.getRating());

        return this.reviewRepository.save(forUpdate);
    }
}
