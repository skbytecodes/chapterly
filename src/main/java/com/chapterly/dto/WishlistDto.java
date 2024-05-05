package com.chapterly.dto;

import com.chapterly.entity.Book;
import com.chapterly.entity.User;

import java.sql.Timestamp;

public class WishlistDto {
    private Long wishlistId;
    private User user;
    private Book book;
    private Timestamp addedDate;


    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Timestamp getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Timestamp addedDate) {
        this.addedDate = addedDate;
    }
}
