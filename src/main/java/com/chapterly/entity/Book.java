package com.chapterly.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    @NotEmpty(message = "Book title cannot be null")
    @Column(unique = true)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "author_id", referencedColumnName = "authorId")
    private Author author;
    @JsonIgnore
    private String ISBN;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_categories_map", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "bookId"), inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "categoryId"))
//    @JsonBackReference
    @JsonIgnore
    private List<Category> categories;
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private String edition;
    private int pages;
    private String description;
    private Double price;
    private String image_url;
    private String imageName;
    private String format; // ebook, hardcover, paperback

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<Review> reviews;

    private int discount;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", ISBN='" + ISBN + '\'' +
                ", categories=" + categories +
                ", publisher='" + publisher + '\'' +
                ", publicationDate=" + publicationDate +
                ", language='" + language + '\'' +
                ", edition='" + edition + '\'' +
                ", pages=" + pages +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image_url='" + image_url + '\'' +
                ", imageName='" + imageName + '\'' +
                ", format='" + format + '\'' +
                ", reviews=" + reviews +
                ", discount=" + discount +
                '}';
    }
}
