package com.chapterly.controller;

import com.chapterly.dto.WishlistDto;
import com.chapterly.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    Logger logger = LoggerFactory.getLogger("WishlistController");

    @PostMapping("/addToWishlist")
    public ResponseEntity<Object> addToWishlist(WishlistDto wishlistDto) {
        try {
            WishlistDto wishlist = wishlistService.addWishlistObject(wishlistDto);
            if (wishlist != null)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/wishlist/{id}")
    public ResponseEntity<?> geItemFromWishlist(@PathVariable("id") Long wishlistItemId) {
        if (wishlistItemId != null) {
            WishlistDto wishlistItem = null;
            try {
                wishlistItem = wishlistService.getWishlistObjectById(wishlistItemId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(wishlistItem, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @PutMapping("/wishlist/{id}")
    public ResponseEntity<?> updateWishlistItem(@PathVariable("id") Long wishlistItemId, WishlistDto wishlistDto) {
        if (wishlistItemId != null && wishlistDto != null) {
            WishlistDto wishlistItem = null;
            try {
                wishlistItem = wishlistService.updateWishlistObjectById(wishlistItemId, wishlistDto);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(wishlistItem, HttpStatus.OK);

        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<?> deleteShippingById(@PathVariable("id") Long wishlistItemId) {
        if (wishlistItemId != null) {
            boolean res = false;
            try {
                res = wishlistService.deleteWishlistObjectById(wishlistItemId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (res)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("FAILED", HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @GetMapping("/deleteWishlistItem/{userId}/{bookId}")
    public ResponseEntity<?> deleteWishlistObjectByUserIdAndBook(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        if (userId != null && bookId != null) {
            List<WishlistDto> wishlist = null;
            try {
                wishlist = wishlistService.deleteWishlistObjectByUserIdAndBook(userId,bookId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(wishlist, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @GetMapping("/deleteWishlistItemByUserId/{userId}")
    public ResponseEntity<?> deleteWishlistItemByUserId(@PathVariable("userId") Long userId) {
        if (userId != null) {
            List<WishlistDto> wishlist = null;
            try {
                wishlist = wishlistService.wishlistByUserId(userId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(wishlist, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
