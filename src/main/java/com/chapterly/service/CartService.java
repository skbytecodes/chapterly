package com.chapterly.service;

import com.chapterly.dto.CartRequest;
import com.chapterly.entity.Cart;

import java.security.Principal;

public interface CartService {
    public Cart addToCart(Principal principal, CartRequest cartItem);
    public Cart getCartById(Long cartItemId);
    public Cart updateCart(Long id, CartRequest cartItem);
    public Cart deleteCart(Long cartItemId);
    public Cart getCartByUserId(Principal principal);

}
