package com.chapterly.mapper;

import com.chapterly.dto.CategoryDto;
import com.chapterly.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "imageUrl", source = "category.imageUrl")
    @Mapping(target = "imageName", source = "category.imageName")
    @Mapping(target = "booksCount", source = "category.booksCount")
    CategoryDto toDto(Category category);
}
