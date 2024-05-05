package com.chapterly.mapper;


import com.chapterly.dto.GenreDto;
import com.chapterly.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @Mapping(target = "name", source = "genre.name")
    @Mapping(target = "description", source = "genre.description")
    GenreDto toDto(Genre genre);
}
