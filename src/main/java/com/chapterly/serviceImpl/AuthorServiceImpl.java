package com.chapterly.serviceImpl;

import com.chapterly.aws.AmazonClient;
import com.chapterly.dto.AuthorDto;
import com.chapterly.entity.Author;
import com.chapterly.mapper.AuthorMapper;
import com.chapterly.repository.AuthorRepo;
import com.chapterly.service.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private AuthorMapper authorMapper;

    @Value("${s3.endpointUrl}")
    private String parentUrl;

    Logger logger = LoggerFactory.getLogger("AuthorServiceImpl");

    @Override
    public AuthorDto saveAuthorDetails(MultipartFile file, String data) {
        Author author = null;
        if (data != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
                author = objectMapper.readValue(data, Author.class);
            } catch (JsonProcessingException e) {
                logger.error("ERROR ", e);
                return null;
            }
        }

        if (file != null) {
            try {
                String fileDownloadUri = amazonClient.uploadFile(file);
                author.setImageUrl(fileDownloadUri);
                author.setImageName(file.getOriginalFilename());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Author savedAuthor = authorRepo.save(author);
        AuthorDto authorResponse = authorMapper.toDto(savedAuthor);
        if (savedAuthor != null) {
            authorResponse.setImageUrl(parentUrl + "/" + author.getImageUrl());
        }
        return authorResponse;
    }

    @Override
    @Cacheable(value = "author", key = "#p0")
    public AuthorDto getAuthorDetailsById(Long authorId) {
        AuthorDto author = null;
        if (authorId != null) {
            author = authorMapper.toDto(authorRepo.findById(authorId).get());
            if (author != null)
                author.setImageUrl(parentUrl + "/" + author.getImageUrl());
        }
        return author;
    }

    @Override
    @CachePut(cacheNames = "author", key = "#p0")
    public AuthorDto updateAuthorDetailsById(Long authorId, AuthorDto authorDto) {
        AuthorDto author = null;
        if (authorDto != null) {
            Author authorById = authorRepo.findById(authorId).get();
            if (authorById != null) {
                authorById.setAwards(authorDto.getAwards());
                authorById.setNationality(authorDto.getNationality());
                authorById.setWebsiteURL(authorDto.getWebsiteURL());
                authorById.setBiography(authorDto.getBiography());
                authorById.setDateOfBirth(authorDto.getDateOfBirth());
                authorById.setCategorySpecialization(authorDto.getCategorySpecialization());
                authorById.setName(authorDto.getName());
            }
            author = authorMapper.toDto(authorById);
            if (author != null)
                author.setImageUrl(parentUrl + "/" + author.getImageUrl());
        }
        return author;
    }

    @Override
    @CacheEvict(cacheNames = "author", key = "#p0")
    public boolean deleteAuthorDetailsById(Long authorId) {
        if (authorId != null) {
            Author author = authorRepo.findById(authorId).get();
            if (author != null) {
                authorRepo.deleteById(authorId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    @Cacheable(value = "author", key = "#p0")
    public List<AuthorDto> searchAuthorsByKeyword(String keyword) {
        if (keyword != null) {
            List<Author> authorByKeyword = authorRepo.findAuthorByKeyword(keyword);
            List<AuthorDto> authors = authorByKeyword.stream().map(auth -> {
                if (auth != null)
                    auth.setImageUrl(parentUrl + "/" + auth.getImageUrl());
                return authorMapper.toDto(auth);
            }).collect(Collectors.toList());
            return authors;
        }
        return new ArrayList<>();
    }

    @Override
    public List<AuthorDto> getAuthorsByGenre(Long categoryId) {
        return null;
    }

    @Override
    public List<AuthorDto> getAuthorsByNationality(String nationality) {
        if (nationality != null && nationality.length() > 0) {
            List<Author> authorsByCountry = authorRepo.findAuthorsByNationality(nationality);
            List<AuthorDto> authors = null;
            if (authorsByCountry.size() > 0) {
                authors = authorsByCountry.stream().map(auth -> {
                    AuthorDto author = authorMapper.toDto(auth);
                    if (auth != null)
                        author.setImageUrl(parentUrl + "/" + author.getImageUrl());
                    return author;
                }).collect(Collectors.toList());
            }
            return authors;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "authors", key = "'authors' + #root.methodName")
    public List<AuthorDto> getAllAuthors() {
        List<Author> allAuthors = authorRepo.findAll();
        List<AuthorDto> authors = null;
        if (allAuthors.size() > 0) {
            authors = allAuthors.stream().map(author -> {
                if (author != null)
                    author.setImageUrl(parentUrl + "/" + author.getImageUrl());
                return authorMapper.toDto(author);
            }).collect(Collectors.toList());
        }
        return authors;
    }

    @Override
    @Cacheable(value = "authorById", key = "#p0")
    public Author getAuthorById(Long authorId) {
        Author author = null;
        try {
            author = authorRepo.findById(authorId).get();
            if (author != null)
                author.setImageUrl(parentUrl + "/" + author.getImageUrl());
        } catch (Exception e) {
            logger.error("ERROR" + e);
            return null;
        }
        return author;
    }

    @Override
    @Cacheable(value = "authorByName", key = "#p0")
    public Author getAuthorByName(String name) {
        Author author = null;
        try {
            author = authorRepo.findByName(name);
            if (author != null)
                author.setImageUrl(parentUrl + "/" + author.getImageUrl());
        } catch (Exception e) {
            logger.error("ERROR", e);
            return null;
        }
        return author;
    }


    @Cacheable(value = "authorByName", key = "#p0")
    public AuthorDto findAuthorByName(String name) {
        Author author = null;
        try {
            author = authorRepo.findByName(name);
            AuthorDto authorDto = null;
            if (author != null) {
                authorDto = authorMapper.toDto(author);
                authorDto.setImageUrl(parentUrl + "/" + author.getImageUrl());
            }
            return authorDto;
        } catch (Exception e) {
            logger.error("ERROR", e);
            return null;
        }
    }

    @Override
    @Cacheable(value = "featuredAuthors", key = "'featuredAuthors'")
    public List<Author> getFeaturedAuthors(int count) {
        try {
            List<Author> featuredAuthors = authorRepo.getFeaturedAuthors(count);
            List<Author> authors = featuredAuthors.stream().map(auth -> {
                auth.setImageUrl(parentUrl + "/" + auth.getImageUrl());
                return auth;
            }).collect(Collectors.toList());
            return authors;
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
        return new ArrayList<>();
    }
}
