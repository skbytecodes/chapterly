package com.chapterly.dto;

import com.chapterly.entity.Book;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    private LocalDateTime orderDate;
    private String status; //pending, processing, shipped, delivered
    private Double totalAmount;
    private String paymentMethod;
    private String razorpayOrderId;
    private List<Book> orderedBooks;

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public List<Book> getOrderedBooks() {
        return orderedBooks;
    }

    public void setOrderedBooks(List<Book> orderedBooks) {
        this.orderedBooks = orderedBooks;
    }
}
