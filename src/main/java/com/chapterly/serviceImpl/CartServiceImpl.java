package com.chapterly.serviceImpl;

import com.chapterly.dto.CartItemRequest;
import com.chapterly.dto.CartRequest;
import com.chapterly.entity.Book;
import com.chapterly.entity.Cart;
import com.chapterly.entity.CartItem;
import com.chapterly.entity.User;
import com.chapterly.repository.CartRepo;
import com.chapterly.service.BookService;
import com.chapterly.service.CartItemService;
import com.chapterly.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;

    public CartServiceImpl(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private CartItemService cartItemService;


    @Override
    public Cart addToCart(Principal principal, CartRequest cartRequest) {
        try {
            var  user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            Cart cart = getUserCart(user);
            List<CartItem> cartItems = cart.getCartItems();
            for (CartItemRequest item : cartRequest.getCartItems()) {
                CartItem cartItem = new CartItem();
                Book book = bookService.getBookByTitle(item.getBook().getTitle());
                cartItem.setBook(book);
                cartItem.setPrice(book.getPrice());
                cartItem.setCart(cart);
                CartItem savedCartItem = cartItemService.saveCartItem(cartItem);
                cartItems.add(savedCartItem);
            }
            cart.setCartItems(cartItems);
            return cartRepo.save(cart);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Cart getUserCart(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null");
        Cart cart = null;
        cart = cartRepo.findCartByUser(user.getUserId());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepo.save(cart);
        }
        return cart;
    }

    @Override
    public Cart getCartById(Long cartItemId) {
        return null;
    }

    @Override
    public Cart updateCart(Long id, CartRequest cartItem) {
        return null;
    }

    @Override
    public Cart deleteCart(Long cartItemId) {
        return null;
    }

    @Override
    public Cart getCartByUserId(Principal principal) {
        try {
            var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            return getUserCart(user);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
