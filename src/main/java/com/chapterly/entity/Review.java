package com.chapterly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "book_id",
            referencedColumnName = "bookId"
    )
    private Book book;
    private int rating;
    private String title;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String body;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp submittedDate;
    private boolean approvedByAdmin;


    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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
}
