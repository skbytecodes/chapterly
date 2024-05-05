package com.chapterly.mapper;

import com.chapterly.dto.AuthorDto;
import com.chapterly.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.Serializable;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    @Mapping(target = "authorId", source = "author.authorId")
    @Mapping(target = "name", source = "author.name")
    @Mapping(target = "dateOfBirth", source = "author.dateOfBirth")
    @Mapping(target = "biography", source = "author.biography")
    @Mapping(target = "nationality", source = "author.nationality")
    @Mapping(target = "categorySpecialization", source = "author.categorySpecialization")
    @Mapping(target = "websiteURL", source = "author.websiteURL")
    @Mapping(target = "imageUrl", source = "author.imageUrl")
    @Mapping(target = "imageName", source = "author.imageName")
    @Mapping(target = "awards", source = "author.awards")
    public AuthorDto toDto(Author author);
}
