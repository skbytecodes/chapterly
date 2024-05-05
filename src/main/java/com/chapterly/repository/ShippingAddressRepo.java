package com.chapterly.repository;

import com.chapterly.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingAddressRepo extends JpaRepository<ShippingAddress, Long> {

    @Query(nativeQuery = true)
    ShippingAddress findByUserId(@Param("userId") Long userId);
    @Query(nativeQuery = true)
    ShippingAddress deleteByUserId(@Param("userId") Long userId);
}

