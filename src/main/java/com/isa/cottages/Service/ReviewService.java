package com.isa.cottages.Service;

import com.isa.cottages.Model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findAll();

    Review findById(Long id) throws Exception;

    Review save(Review review);

    Review update(Review review);
}
