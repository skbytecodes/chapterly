package com.chapterly.dto;

import com.chapterly.entity.Book;
import com.chapterly.entity.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReviewDto implements Serializable {
    private Long id;
    private User user;
    private Book book;
    private int rating; // stars
    private String title;
    private String body;
    private Timestamp submittedDate;
    private boolean approvedByAdmin;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
    }

    public boolean isApprovedByAdmin() {
        return approvedByAdmin;
    }

    public void setApprovedByAdmin(boolean approvedByAdmin) {
        this.approvedByAdmin = approvedByAdmin;
    }


}
