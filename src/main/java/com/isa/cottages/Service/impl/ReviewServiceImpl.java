package com.isa.cottages.Service.impl;


import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.ReviewRepository;
import com.isa.cottages.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {


    private ReviewRepository reviewRepository;
    private BoatServiceImpl boatService;
    private CottageServiceImpl cottageService;
    private FishingInstructorAdventureServiceImpl adventureService;
    private ClientServiceImpl userService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, BoatServiceImpl boatService, CottageServiceImpl cottageService, FishingInstructorAdventureServiceImpl adventureService, ClientServiceImpl userService)
    {
        this.reviewRepository = reviewRepository;
        this.boatService = boatService;
        this.cottageService = cottageService;
        this.adventureService = adventureService;
        this.userService = userService;
    }

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

        forUpdate.setAdventure(review.getAdventure());
        forUpdate.setBoat(review.getBoat());
        forUpdate.setApproved(review.getApproved());
        forUpdate.setCottage(review.getCottage());
        forUpdate.setClient(review.getClient());
        forUpdate.setReviewText(review.getReviewText());
        forUpdate.setRating(review.getRating());

        return this.reviewRepository.save(forUpdate);
    }
    public List<Review> findNotApprovedReviews()throws Exception
    {
        return this.reviewRepository.findReviewByApprovedIsFalse();
    }

    public Review postReview (Long rid) throws Exception {
        Review review = this.findById(rid);
        Client client = (Client) this.userService.findById(review.getClient().getId());
        if (review.getBoat()!=null)
        {
            Boat boat=this.boatService.findById(review.getBoat().getId());
            boat.setAverageRating(boat.getAverageRating() + review.getRating()/boat.getReviews().size() +1);
            Set<Review> boatReviews = boat.getReviews();
            boatReviews.add(review);
            boat.setReviews(boatReviews);
            this.boatService.updateBoat(boat);
            review.setApproved(Boolean.TRUE);
            this.update(review);
        }
        else if (review.getCottage()!=null)
        {
            Cottage cottage=this.cottageService.findById(review.getCottage().getId());
            cottage.setAverageRating(cottage.getAverageRating() + review.getRating()/cottage.getReviews().size() +1);
            Set<Review> cottageReviews = cottage.getReviews();
            cottageReviews.add(review);
            cottage.setReviews(cottageReviews);
            this.cottageService.updateCottage(cottage);
            review.setApproved(Boolean.TRUE);
            this.update(review);
        }
        else if (review.getAdventure()!=null)
        {
            FishingInstructorAdventure adventure=this.adventureService.findById(review.getAdventure().getId());
            adventure.setAverageRating(adventure.getAverageRating() + review.getRating()/adventure.getReviews().size() +1);
            Set<Review> adventureReviews = adventure.getReviews();
            adventureReviews.add(review);
            adventure.setReviews(adventureReviews);
            this.adventureService.updateAdventure(adventure);
            review.setApproved(Boolean.TRUE);
            this.update(review);
        }
        return review;
    }
}
