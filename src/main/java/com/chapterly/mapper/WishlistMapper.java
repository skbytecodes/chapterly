package com.chapterly.mapper;

import com.chapterly.dto.WishlistDto;
import com.chapterly.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistMapper {


    @Mapping(target = "wishlistId", source = "wishlist.wishlistId")
    @Mapping(target = "user", source = "wishlist.user")
    @Mapping(target = "book", source = "wishlist.book")
    @Mapping(target = "addedDate", source = "wishlist.addedDate")
    WishlistDto toDto(Wishlist wishlist);
}
