package com.chapterly.controller;


import com.chapterly.dto.ReviewDto;
import com.chapterly.dto.ReviewResponse;
import com.chapterly.records.RatingResponse;
import com.chapterly.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    Logger logger = LoggerFactory.getLogger("ReviewController");

    @PostMapping("/saveReview")
    public ResponseEntity<Object> saveReview(@RequestBody ReviewDto reviewDto){
        try {
            ReviewDto review = reviewService.saveReview(reviewDto);
            if(review == null)
                return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
            if(review.getBody().equals("ALREADY REVIEWED"))
                return new ResponseEntity<>("ALREADY REVIEWED", HttpStatus.OK);
            return new ResponseEntity<>("SUCCESSFULLY REVIEWED", HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<?> reviewById(@PathVariable("id") Long reviewId) {
        if (reviewId != null) {
            ReviewDto review = null;
            try {
                review = reviewService.getReviewById(reviewId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @PutMapping("/review/{id}")
    public ResponseEntity<?> updateReview(@PathVariable("id") Long reviewId, ReviewDto reviewRequest) {
        if (reviewId != null) {
            ReviewDto review = null;
            try {
                review = reviewService.updateReview(reviewId, reviewRequest);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long reviewId) {
        if (reviewId != null) {
            boolean res = false;
            try {
                res = reviewService.deleteReview(reviewId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (res)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("FAILED", HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/reviews/{bookTitle}")
    public ResponseEntity<?> getReviewsForBook(@PathVariable("bookTitle") String bookTitle) {
        try {
            List<ReviewDto> reviews = reviewService.getReviewsForBook(bookTitle);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reviews/book/{bookName}")
    public ResponseEntity<?> getReviewsByBookName(@PathVariable("bookName") String bookName) {
        try {
            List<ReviewResponse> reviews = reviewService.getReviewsByBookName(bookName);
            if(reviews != null || reviews.size() > 0)
                return new ResponseEntity<>(reviews, HttpStatus.OK);
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/book/rating/{bookTitle}")
    public ResponseEntity<Object> getAverageRatingForBook(@PathVariable("bookTitle") String bookTitle){
        RatingResponse rating = null;
        if(bookTitle != null){
            try {
                rating = reviewService.getAverageRatingForBook(bookTitle);
            }catch (Exception e){
                logger.error("ERROR", e);
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }



    @GetMapping("/reviewsByUser/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable("userId") Long userId) {
        try {
            List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getLatestReviews/{count}")
    public ResponseEntity<?> getLatestReviews(@PathVariable("count") int count) {
        try {
            List<ReviewDto> reviews = reviewService.getLatestReviews(count);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTotalReviewsCount")
    public ResponseEntity<?> getTotalReviewsCount() {
        try {
            int count = reviewService.getTotalReviewCount();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
