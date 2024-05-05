package com.chapterly.repository;

import com.chapterly.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {
    @Query(nativeQuery = true)
    List<Author> findAuthorByKeyword(@Param("str") String str);

    @Query(nativeQuery = true)
    List<Author> findAuthorsByNationality(@Param("nationality") String nationality);

    Author findByName(String name);

    @Query(nativeQuery = true)
    List<Author> getFeaturedAuthors(@Param("limit") int count);

    @Query(nativeQuery = true)
    List<String> getAuthorsByName(@Param("key") String key);
}
