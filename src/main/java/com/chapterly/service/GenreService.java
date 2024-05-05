package com.chapterly.service;

import com.chapterly.dto.GenreDto;
import com.chapterly.entity.Genre;

import java.util.List;

public interface GenreService {

    GenreDto addGenre(GenreDto GenreDto);
    GenreDto getGenreById(Long genreId);

    boolean deleteGenreById(Long genreId);

    Genre getGenreByName(String name);
    boolean addGenres(List<GenreDto> genresRequest);
    List<GenreDto> getAllGenres();
}
