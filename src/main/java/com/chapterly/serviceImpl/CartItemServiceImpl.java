package com.chapterly.serviceImpl;

import com.chapterly.dto.CartItemRequest;
import com.chapterly.entity.CartItem;
import com.chapterly.repository.CartItemRepo;
import com.chapterly.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepo cartItemRepo;

    public CartItemServiceImpl(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public CartItem saveCartItem(CartItemRequest cartItem) {
        if (cartItem == null)
            throw new IllegalArgumentException("CartItem cannot be null");
        try {
            CartItem item = new CartItem();
            item.setBook(cartItem.getBook());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            return cartItemRepo.save(item);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving CartItem");
        }
    }

    @Override
    public CartItem saveCartItem(CartItem cartItem) {
        if(cartItem == null)
            throw new IllegalArgumentException("CartItem cannot be null");
        try {
            return cartItemRepo.save(cartItem);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving CartItem");
        }
    }

    @Override
    public CartItem getCartItemById(Long cartItemId) {
        return null;
    }

    @Override
    public CartItem updateCartItem(Long id, CartItemRequest cartItem) {
        return null;
    }

    @Override
    public CartItem deleteCartItem(Long cartItemId) {
        return null;
    }

    @Override
    public List<CartItem> getCartItemsByCartId(Long cartId) {
        return null;
    }
}
