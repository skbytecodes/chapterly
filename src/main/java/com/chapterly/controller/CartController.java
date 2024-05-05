package com.chapterly.controller;


import com.chapterly.dto.CartRequest;
import com.chapterly.entity.Cart;
import com.chapterly.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(Principal user, @RequestBody CartRequest cartRequest){
        try {
            Cart cart = cartService.addToCart(user, cartRequest);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/userCart")
    public ResponseEntity<?> getCart(Principal principal){
        try {
            Cart cart = cartService.getCartByUserId(principal);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
