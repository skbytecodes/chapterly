package com.chapterly.mapper;

import com.chapterly.dto.ReviewResponse;
import com.chapterly.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ReviewResponseMapper {
    @Mapping(target = "userName", source = "review.user.userName")
    @Mapping(target = "bookName", source = "review.book.title")
    @Mapping(target = "body", source = "review.body")
    @Mapping(target = "title", source = "review.title")
    @Mapping(target ="rating", source = "review.rating")
    @Mapping(target = "date", source = "review.submittedDate")
    ReviewResponse toDto(Review review);
}
