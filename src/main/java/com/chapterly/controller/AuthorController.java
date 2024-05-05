package com.chapterly.controller;

import com.chapterly.dto.AuthorDto;
import com.chapterly.entity.Author;
import com.chapterly.mapper.AuthorMapper;
import com.chapterly.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorMapper authorMapper;

    @Value("${s3.endpointUrl}")
    private String parentUrl;

    Logger logger = LoggerFactory.getLogger("AuthorController");


    @PostMapping("/saveAuthor")
    public ResponseEntity<Object> saveAuthor(@RequestParam(value = "file",required = false) MultipartFile file, String data ){
        try {
            AuthorDto author = authorService.saveAuthorDetails(file, data);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<Object> getAuthorDetailsById(@PathVariable("id") Long authorId){
        try {
            AuthorDto author = authorService.getAuthorDetailsById(authorId);
            if(author != null)
                return new ResponseEntity<>(author, HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("ERROR", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }


    @PutMapping("/updateAuthor/{id}")
    public ResponseEntity<Object> updateAuthorDetailsById(@PathVariable("id") Long authorId, AuthorDto authorRequest){
        try {
            AuthorDto author = authorService.updateAuthorDetailsById(authorId,authorRequest);
            if(author != null)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("ERROR", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteAuthor/{id}")
    public ResponseEntity<Object> deleteAuthorDetailsById(@PathVariable("id") Long authorId, AuthorDto authorRequest){
        try {
            boolean isDeleted = authorService.deleteAuthorDetailsById(authorId);
            if(isDeleted)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("ERROR", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }

    @GetMapping("/searchAuthors/{keyword}")
    public ResponseEntity<Object> getAllAuthorsByKeyword(@PathVariable("keyword") String keyword){
        try {
            List<AuthorDto> authors = authorService.searchAuthorsByKeyword(keyword);
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR ",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }

    @GetMapping("/searchAuthorsByNationality/{nationality}")
    public ResponseEntity<Object> searchAuthorsByNationality(@PathVariable("nationality") String nationality){
        try {
            List<AuthorDto> authors = authorService.getAuthorsByNationality(nationality);
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR ",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }


    @GetMapping("/authors")
    public ResponseEntity<Object> getAllAuthors(){
        try {
            List<AuthorDto> authors = authorService.getAllAuthors();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR ",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }

    @GetMapping("/featuredAuthors/{count}")
    public ResponseEntity<Object> getAllFeaturedAuthors(@PathVariable("count") int count){
        try {
            List<Author> authors = authorService.getFeaturedAuthors(count);
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR ",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }


    @GetMapping("/authors/authorByName/{authorName}")
    public ResponseEntity<?> getAuthorByName(@PathVariable("authorName") String authorName){
        try {
            AuthorDto author = authorService.findAuthorByName(authorName);
//            AuthorDto author = authorMapper.toDto(authorByName);
            return new ResponseEntity<>(author, HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR ",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.OK);
        }
    }
}
