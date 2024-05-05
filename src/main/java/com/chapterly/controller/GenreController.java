package com.chapterly.controller;

import com.chapterly.dto.CategoryDto;
import com.chapterly.dto.GenreDto;
import com.chapterly.entity.Genre;
import com.chapterly.service.CategoryService;
import com.chapterly.service.GenreService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v1")
public class GenreController {


    @Autowired
    private GenreService genreService;

    Logger logger = LoggerFactory.getLogger("GenreController");

    @PostMapping("/addNewGenre")
    public ResponseEntity<Object> addNewGenre(@Valid @RequestBody GenreDto genreDto) {
        try {
            GenreDto savedGenre = genreService.addGenre(genreDto);
            if (savedGenre == null)
                return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addGenres")
    public ResponseEntity<Object> addListOfGenres(@Valid @RequestBody List<GenreDto> genres) {

        try {
            boolean savedGenre = genreService.addGenres(genres);
            if (savedGenre == false)
                return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/genreById/{id}")
    public ResponseEntity<Object> getGenreById(@PathVariable("id") Long genreId) {
        try {
            GenreDto genre = genreService.getGenreById(genreId);
            if (genre != null) {
                return new ResponseEntity<>(genre, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("ERROR ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteGenreById/{id}")
    public ResponseEntity<Object> deleteGenreById(@PathVariable("id") Long genreId) {
        try {
            boolean b = genreService.deleteGenreById(genreId);
            if(b == false)
                return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllGenres")
    public ResponseEntity<Object> getAllGenres() {
        try {
            List<GenreDto> allGenres = genreService.getAllGenres();
            return new ResponseEntity<>(allGenres, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
