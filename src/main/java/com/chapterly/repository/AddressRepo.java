package com.chapterly.repository;

import com.chapterly.entity.Address;
import com.chapterly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    @Query(nativeQuery = true)
    Address findAddressByUserId(@Param("userId") Long userId);
}
