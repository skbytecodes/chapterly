package com.chapterly.serviceImpl;

import com.chapterly.dto.WishlistDto;
import com.chapterly.entity.Wishlist;
import com.chapterly.mapper.WishlistMapper;
import com.chapterly.repository.WishlistRepo;
import com.chapterly.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private WishlistMapper wishlistMapper;
    Logger logger = LoggerFactory.getLogger("WishlistServiceImpl");

    @Override
    public WishlistDto addWishlistObject(WishlistDto wishlistDto) {
        WishlistDto wishlistResponse = null;
        if(wishlistDto != null){
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(wishlistDto.getUser());
            wishlist.setBook(wishlistDto.getBook());
            wishlist.setAddedDate(wishlistDto.getAddedDate());
            try {
                Wishlist savedWishlist = wishlistRepo.save(wishlist);
                wishlistResponse = wishlistMapper.toDto(savedWishlist);
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return wishlistResponse;
    }

    @Override
    public WishlistDto getWishlistObjectById(Long wishlistId) {
        if(wishlistId != null){
            return wishlistMapper.toDto(wishlistRepo.findById(wishlistId).get());
        }
        return null;
    }

    @Override
    public WishlistDto updateWishlistObjectById(Long wishlistId, WishlistDto wishlistDto) {
        WishlistDto wishlistResponse = null;
        if(wishlistDto != null){
            try {
                Wishlist wishlistById = wishlistRepo.findById(wishlistId).get();
                wishlistById.setUser(wishlistDto.getUser());
                wishlistById.setBook(wishlistDto.getBook());
                wishlistById.setAddedDate(wishlistDto.getAddedDate());

                wishlistRepo.save(wishlistById);
                wishlistResponse = wishlistMapper.toDto(wishlistById);
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return wishlistResponse;
    }

    @Override
    public boolean deleteWishlistObjectById(Long wishlistObjectId) {
        if(wishlistObjectId != null){
            Wishlist wishlist = wishlistRepo.findById(wishlistObjectId).get();
            if(wishlist != null){
                wishlistRepo.deleteById(wishlistObjectId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<WishlistDto> deleteWishlistObjectByUserIdAndBook(Long userId, Long bookId) {
        List<WishlistDto> wishlistResponse = null;
        if(userId != null && bookId != null) {
            try {
                Wishlist wishlist = wishlistRepo.deleteWishlistObjectByUserIdAndBook(userId,bookId);
                List<Wishlist> wishlistCollection = wishlistRepo.getAllWishlistItemsByUserId(userId);
                wishlistResponse = wishlistCollection.stream().map(wish -> {
                    return wishlistMapper.toDto(wish);
                }).collect(Collectors.toList());
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return wishlistResponse;
    }

    @Override
    public List<WishlistDto> wishlistByUserId(Long userId) {
        List<WishlistDto> wishlistResponse = null;
        if(userId != null) {
            try {
                List<Wishlist> wishlistCollection = wishlistRepo.getAllWishlistItemsByUserId(userId);
                wishlistResponse = wishlistCollection.stream().map(wish -> {
                    return wishlistMapper.toDto(wish);
                }).collect(Collectors.toList());
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return wishlistResponse;
    }
}
