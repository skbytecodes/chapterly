package com.chapterly.repository;

import com.chapterly.dto.OrderDtoInterf;
import com.chapterly.dto.OrderResponse;
import com.chapterly.entity.Order;
import com.chapterly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query(nativeQuery = true)
    Order findByRazorpayOrderId(@Param("orderId") String orderId);


    @Query(nativeQuery = true)
    List<OrderDtoInterf> findAllOrdersByUser(@Param("user") Long user);
}
