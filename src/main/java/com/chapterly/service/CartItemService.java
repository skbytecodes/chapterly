package com.chapterly.service;

import com.chapterly.dto.CartItemRequest;
import com.chapterly.dto.CartRequest;
import com.chapterly.entity.Cart;
import com.chapterly.entity.CartItem;

import java.util.List;

public interface CartItemService {
    public CartItem saveCartItem(CartItemRequest cartItem);
    public CartItem saveCartItem(CartItem cartItem);
    public CartItem getCartItemById(Long cartItemId);
    public CartItem updateCartItem(Long id, CartItemRequest cartItem);
    public CartItem deleteCartItem(Long cartItemId);
    public List<CartItem> getCartItemsByCartId(Long cartId);
}
