package com.chapterly.serviceImpl;

import com.chapterly.dto.GenreDto;
import com.chapterly.entity.Genre;
import com.chapterly.mapper.GenreMapper;
import com.chapterly.repository.GenreRepo;
import com.chapterly.service.GenreService;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    Logger logger = LoggerFactory.getLogger("GenreServiceImpl");
    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private GenreMapper genreMapper;
    @Override
    public GenreDto addGenre(GenreDto genreDto) {
        GenreDto genreResponse = null;
        try {
            if(genreDto != null){
                Genre genre = new Genre();
                genre.setName(genreDto.getName());
                genre.setDescription(genreDto.getDescription());
                Genre savedGenre = genreRepo.save(genre);
                genreResponse = genreMapper.toDto(savedGenre);
            }
        }catch (Exception e){
            logger.error("ERROR", e);
            return null;
        }
        return genreResponse;
    }

    public boolean addGenres(List<GenreDto> genresRequest){
        try {
            if(genresRequest != null && genresRequest.size() > 0){
                List<Genre> genres = genresRequest.stream().map(genreDto -> {
                    Genre genre = new Genre();
                    genre.setName(genreDto.getName());
                    genre.setDescription(genreDto.getDescription());
                    return genre;
                }).collect(Collectors.toList());

                List<Genre> savedGenres = genreRepo.saveAll(genres);
                if(savedGenres != null)
                    return true;
            }
        }catch (Exception e){
            logger.error("ERROR", e);
            return false;
        }
        return false;
    }

    @Override
    public List<GenreDto> getAllGenres() {
        try {
            List<Genre> all = genreRepo.findAll();
            List<GenreDto> genres = new ArrayList<>();
            if(all != null && all.size()>0){
                genres = all.stream().map(genre -> {
                    return genreMapper.toDto(genre);
                }).collect(Collectors.toList());
            }
            return genres;
        }catch (Exception e){
            logger.error("ERROR",e);
            return new ArrayList<>();
        }
    }

    @Override
    public GenreDto getGenreById(Long genreId) {
        GenreDto genreResponse = null;
        try {
            Genre genre = genreRepo.findById(genreId).get();
            genreResponse = genreMapper.toDto(genre);
        }catch (Exception e){
            logger.error("ERROR", e);
            return null;
        }
        return genreResponse;
    }

    @Override
    public boolean deleteGenreById(Long genreId) {
        try {
            genreRepo.deleteById(genreId);
            return true;
        }catch (Exception e){
            logger.error("ERROR",e);
            return false;
        }
    }

    @Override
    public Genre getGenreByName(String name) {
        Genre genre = null;
        try {
            genre = genreRepo.findByName(name);
        }catch (Exception e){
            logger.error("ERROR",e);
        }
        return genre;
    }
}
