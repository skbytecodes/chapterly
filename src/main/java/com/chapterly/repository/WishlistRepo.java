package com.chapterly.repository;

import com.chapterly.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Long> {

    @Query(nativeQuery = true)
    Wishlist getWishlistObjectByUserIdAndBook(@Param("userId") Long userId, @Param("bookId") Long bookId);
    @Query(nativeQuery = true)
    Wishlist deleteWishlistObjectByUserIdAndBook(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query(nativeQuery = true)
    List<Wishlist> getAllWishlistItemsByUserId(@Param("userId") Long userId);
}
