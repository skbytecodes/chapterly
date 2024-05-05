package com.chapterly.mapper;

import com.chapterly.dto.ReviewDto;
import com.chapterly.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", source = "review.reviewId")
    @Mapping(target = "user", source = "review.user")
    @Mapping(target = "book", source = "review.book")
    @Mapping(target = "rating", source = "review.rating")
    @Mapping(target = "title", source = "review.title")
    @Mapping(target = "body", source = "review.body")
    @Mapping(target = "submittedDate", source = "review.submittedDate")
    @Mapping(target = "approvedByAdmin", source = "review.approvedByAdmin")
    ReviewDto toDto(Review review);
}
