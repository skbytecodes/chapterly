package com.chapterly.dto;

import com.chapterly.entity.Book;
import com.chapterly.entity.CartItem;
import com.chapterly.entity.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class CartRequest implements Serializable {
    private List<CartItemRequest> cartItems;

    public List<CartItemRequest> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemRequest> cartItems) {
        this.cartItems = cartItems;
    }
}
