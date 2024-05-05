package com.chapterly.repository;

import com.chapterly.entity.Cart;
import com.chapterly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    @Query(nativeQuery = true)
    Cart findCartByUser(@Param("userId") Long userId);
}
