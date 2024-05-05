package com.chapterly.service;

import com.chapterly.dto.WishlistDto;
import com.chapterly.entity.Wishlist;

import java.util.List;

public interface WishlistService {
    public WishlistDto addWishlistObject(WishlistDto wishlistDto);
    public WishlistDto getWishlistObjectById(Long wishlistId);
    public WishlistDto updateWishlistObjectById(Long wishlistId, WishlistDto wishlistDto);
    public boolean deleteWishlistObjectById(Long wishlistObjectId);
    public List<WishlistDto> deleteWishlistObjectByUserIdAndBook(Long userId, Long bookId);
    public List<WishlistDto> wishlistByUserId(Long userId);
}
