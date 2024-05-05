package com.chapterly.repository;

import com.chapterly.dto.ReviewDto;
import com.chapterly.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    @Query(nativeQuery = true)
    List<Review> getAllReviewsForBook(@Param("bookTitle") String bookTitle);

    @Query(nativeQuery = true)
    List<Review> findAllReviewsByUser(@Param("userId") Long userId);

    @Query(nativeQuery = true)
    List<ReviewDto> findLatestReviews(@Param("count") int count);

    @Query(nativeQuery = true)
    int countTotalReviews();

    @Query(nativeQuery = true)
    List<Review> checkIfUserHasAlreadyReviewedTheBook(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query(nativeQuery = true)
    List<Review> getAllReviewsForBookByName(@Param("bookName") String bookName);
}
