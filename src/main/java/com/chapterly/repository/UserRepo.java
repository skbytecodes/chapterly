package com.chapterly.repository;

import com.chapterly.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query(nativeQuery = true)
    User findByUserName(@Param("username") String username);

    @Query(nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(nativeQuery = true)
    List<User> findAllActiveUsers();
}
