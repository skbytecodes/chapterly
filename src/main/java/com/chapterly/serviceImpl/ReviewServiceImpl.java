package com.chapterly.serviceImpl;

import com.chapterly.dto.BookDto;
import com.chapterly.dto.ReviewDto;
import com.chapterly.dto.ReviewResponse;
import com.chapterly.dto.UserDto;
import com.chapterly.entity.Book;
import com.chapterly.entity.Review;
import com.chapterly.entity.User;
import com.chapterly.mapper.ReviewMapper;
import com.chapterly.mapper.ReviewResponseMapper;
import com.chapterly.records.RatingResponse;
import com.chapterly.repository.BookRepo;
import com.chapterly.repository.ReviewRepo;
import com.chapterly.service.BookService;
import com.chapterly.service.ReviewService;
import com.chapterly.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewResponseMapper responseMapper;

    @Autowired
    private UserService userService;

//    @Autowired
//    private BookService bookService;

    @Autowired
    private BookRepo bookRepo;

    Logger logger = LoggerFactory.getLogger("ReviewServiceImpl");

    @Override
    public ReviewDto saveReview(ReviewDto reviewDto) {
        if (reviewDto != null) {
            Review review = new Review();
            User userBYUsername = userService.getUserBYUsername(reviewDto.getUser().getUserName());
            review.setTitle(reviewDto.getTitle());
            review.setApprovedByAdmin(true);
            review.setRating(reviewDto.getRating());
            review.setBody(reviewDto.getBody());
            review.setUser(userBYUsername);
//            Book bookByTitle = bookService.getBookByTitle(reviewDto.getBook().getTitle());
            Book bookByTitle = bookRepo.findBookByName(reviewDto.getBook().getTitle());
            bookByTitle.setImage_url(bookByTitle.getImage_url().replace("https://chapterly-springboot-app.s3.eu-north-1.amazonaws.com/", ""));
            review.setBook(bookByTitle);

            reviewRepo.save(review);
            return reviewDto;
        } else {
            return null;
        }
    }

    @Override
    public ReviewDto getReviewById(Long reviewId) {
        if (reviewId != null) {
            return reviewMapper.toDto(reviewRepo.findById(reviewId).get());
        }
        return null;
    }

    @Override
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) {
        ReviewDto response = null;
        if (reviewDto != null) {
            Review review = null;
            try {
                review = reviewRepo.findById(reviewId).get();
                if (review != null) {
                    review.setBody(reviewDto.getBody());
//                    review.setBook(reviewDto.getBook());
                    review.setRating(reviewDto.getRating());
                    review.setTitle(reviewDto.getTitle());
                    review.setApprovedByAdmin(true);
                }
                response = reviewMapper.toDto(review);
            } catch (Exception e) {
                logger.error("ERROR", e);
            }
        }
        return response;
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        if (reviewId != null) {
            Review review = reviewRepo.findById(reviewId).get();
            if (review != null) {
                reviewRepo.deleteById(reviewId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<ReviewDto> getReviewsForBook(String bookId) {
        List<Review> allReviews = null;
        try {
            allReviews = reviewRepo.getAllReviewsForBook(bookId);
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        List<ReviewDto> reviews = allReviews.stream().map(book -> {
            return reviewMapper.toDto(book);
        }).collect(Collectors.toList());

        if (allReviews.size() > 0) {
            return reviews;
        }
        return new ArrayList<>();
    }

    @Override
    public RatingResponse getAverageRatingForBook(String bookTitle) {
        double averageRating = 0;
        RatingResponse ratingResponse = null;
        try {
            List<Review> allReviewsForBook = reviewRepo.getAllReviewsForBookByName(bookTitle);
            double rating = 0.0;
            int five =0,four=0, three=0, two=0, one = 0;
            for (Review review : allReviewsForBook) {
                rating += review.getRating();
                if(review.getRating() == 5)
                    five+=1;
                else if(review.getRating() == 4)
                    four+=1;
                else if(review.getRating() == 3)
                    three+=1;
                else if(review.getRating() == 2)
                    two+=1;
                else
                    one+=1;
            }
            if(rating != 0.0)
                averageRating = rating / allReviewsForBook.size();
            ratingResponse = new RatingResponse(averageRating,allReviewsForBook.size(), five, four, three, two, one);
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        return ratingResponse;
    }

    @Override
    public List<ReviewDto> getReviewsByUser(Long userId) {
        List<Review> authorReviews = null;
        try {
            authorReviews = reviewRepo.findAllReviewsByUser(userId);
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        List<ReviewDto> reviews = authorReviews.stream().map(review -> {
            return reviewMapper.toDto(review);
        }).collect(Collectors.toList());

        if (authorReviews.size() > 0) {
            return reviews;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean hasUserReviewedBook(Long userId, Long bookId) {
        return false;
    }

    @Override
    public List<ReviewDto> getLatestReviews(int count) {
        List<ReviewDto> latestReviews = null;
        try {
            latestReviews = reviewRepo.findLatestReviews(count);
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        return latestReviews;
    }

    @Override
    public List<ReviewDto> getReviewsForUser(Long userId) {
        return null;
    }

    @Override
    public List<ReviewDto> getBooksUserReviewed(Long userId) {
        return null;
    }

    @Override
    public int getTotalReviewCount() {
        int totalReviewsCount = 0;
        try {
            totalReviewsCount = reviewRepo.countTotalReviews();
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        return totalReviewsCount;
    }

    @Override
    public void flagReviewAsInappropriate(Long reviewId) {

    }

    @Override
    public List<ReviewDto> getAbusiveReviews() {
        return null;
    }

    @Override
    public List<ReviewResponse> getReviewsByBookName(String bookName) {
        List<Review> allReviews = null;
        try {
            allReviews = reviewRepo.getAllReviewsForBookByName(bookName);
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        List<ReviewResponse> reviews = allReviews.stream().map(review -> {
            return responseMapper.toDto(review);
        }).collect(Collectors.toList());

        if (allReviews.size() > 0) {
            return reviews;
        }
        return new ArrayList<>();
    }
}
