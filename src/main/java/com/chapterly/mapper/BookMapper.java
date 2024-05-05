package com.chapterly.mapper;

import com.chapterly.dto.BookDto;
import com.chapterly.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "title", source = "book.title")
    @Mapping(target = "author", source = "book.author")
    @Mapping(target = "ISBN", source = "book.ISBN")
    @Mapping(target = "publisher", source = "book.publisher")
    @Mapping(target = "publicationDate", source = "book.publicationDate")
    @Mapping(target = "language", source = "book.language")
    @Mapping(target = "edition", source = "book.edition")
    @Mapping(target = "pages", source = "book.pages")
    @Mapping(target = "description", source = "book.description")
    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "image_url", source = "book.image_url")
    @Mapping(target = "format", source = "book.format")
    @Mapping(target = "imageName", source = "book.imageName")
    @Mapping(target = "discount", source = "book.discount")
    BookDto toDto(Book book);
}
