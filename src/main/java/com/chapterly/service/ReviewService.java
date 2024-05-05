package com.chapterly.service;

import com.chapterly.dto.ReviewDto;
import com.chapterly.dto.ReviewResponse;
import com.chapterly.entity.Book;
import com.chapterly.entity.Review;
import com.chapterly.records.RatingResponse;

import java.io.StringReader;
import java.util.List;

public interface ReviewService {
    public ReviewDto saveReview(ReviewDto reviewDto);
    public ReviewDto getReviewById(Long reviewId);
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto);
    public boolean deleteReview(Long reviewId);
    public List<ReviewDto> getReviewsForBook(String bookTitle);
    public RatingResponse getAverageRatingForBook(String bookTitle);
    public List<ReviewDto> getReviewsByUser(Long userId);
    public boolean hasUserReviewedBook(Long userId, Long bookId);
    public List<ReviewDto> getLatestReviews(int count);
    public List<ReviewDto> getReviewsForUser(Long userId);
    public List<ReviewDto> getBooksUserReviewed(Long userId);
    public int getTotalReviewCount();
    public void flagReviewAsInappropriate(Long reviewId);
    public List<ReviewDto> getAbusiveReviews();
    public List<ReviewResponse> getReviewsByBookName(String bookName);
}
