package com.chapterly.dto;

import com.chapterly.entity.Book;

import java.io.Serializable;

public class CartItemRequest implements Serializable {
    private Book book;
    private int quantity;
    private Double price;


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
