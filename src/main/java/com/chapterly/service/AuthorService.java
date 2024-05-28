package com.chapterly.service;

import com.chapterly.dto.AuthorDto;
import com.chapterly.entity.Author;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuthorService {
    public AuthorDto saveAuthorDetails(MultipartFile file, String data) throws JsonMappingException;

    public AuthorDto getAuthorDetailsById(Long authorId);

    public AuthorDto updateAuthorDetailsById(Long authorId, AuthorDto authorDto);

    public boolean deleteAuthorDetailsById(Long authorId);

    public List<AuthorDto> searchAuthorsByKeyword(String keyword);

    public List<AuthorDto> getAuthorsByGenre(Long categoryId);

    public List<AuthorDto> getAuthorsByNationality(String nationality);

    public List<AuthorDto> getAllAuthors();

    public Author getAuthorById(Long authorId);

    Author getAuthorByName(String name);
    List<Author> getFeaturedAuthors(int count);
    public AuthorDto findAuthorByName(String name);
}
