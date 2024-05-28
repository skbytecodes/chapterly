package com.chapterly.service;

import com.chapterly.dto.CategoryDto;
import com.chapterly.entity.Category;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    public CategoryDto addCategory(MultipartFile file, String category) throws JsonMappingException, JsonParseException;
    public CategoryDto getCategoryById(Long categoryId);
    public CategoryDto updateCategoryById(Long categoryId, CategoryDto categoryDto);
    public void deleteCategoryByCategoryId(Long categoryId);
    public List<CategoryDto> getAllCategories();
    CategoryDto getCategoryByKey(String key);
    Category findCategoryById(Long categoryId);
    Category findCategoryByName(String categoryName);
    Category getCategoryByName(String categoryName);
}
