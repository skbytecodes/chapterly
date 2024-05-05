package com.chapterly.dto;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

public class GenreDto implements Serializable {

    @NotEmpty(message = "Genre name must not be empty")
    private String name;
    @NotEmpty(message = "Genre description must not be empty")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
