package com.chapterly.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @NotEmpty(message = "author name can not be empty")
    @Column(unique = true)
    private String name;

    @Past(message = "please provide the valid date")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "biography can not be empty")
    private String biography;

    @NotEmpty(message = "nationality can not be empty")
    private String nationality;


//    @JsonBackReference
//    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
           name = "author_genres",
            joinColumns = @JoinColumn(
                    name = "author_id",
                    referencedColumnName = "authorId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "genre_id",
                    referencedColumnName = "genreId"

            )
    )
    private List<Genre> categorySpecialization = new ArrayList<>();
    private String websiteURL;
    private String imageUrl;
    private String imageName;
    private String awards;


    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Genre> getCategorySpecialization() {
        return categorySpecialization;
    }

    public void setCategorySpecialization(List<Genre> categorySpecialization) {
        this.categorySpecialization = categorySpecialization;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
