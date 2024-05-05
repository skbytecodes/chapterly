package com.chapterly.repository;

import com.chapterly.entity.Category;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query(nativeQuery = true)
    Category findCategoryByKeyword(@Param("str") String key);

    Category findByCategoryName(String categoryName);
}
