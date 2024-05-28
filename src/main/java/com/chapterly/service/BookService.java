package com.chapterly.service;

import com.chapterly.dto.BannerDto;
import com.chapterly.dto.BookDto;
import com.chapterly.entity.Book;
import com.chapterly.records.SearchRecord;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    BookDto addBook(MultipartFile file, String data, MultipartFile authorImage) throws JsonMappingException, FileUploadException;
    BookDto getBookById(Long bookId);
    BookDto updateBookDetails(Long bookId, BookDto book);
     boolean deleteBookById(Long bookId);
     List<BookDto> getAllBooks();
     List<BookDto> searchBooksByKeyword(String title);
     BookDto searchByTitle(String data);
     List<BookDto> getBooksByAuthor(Long authorId);
     List<BookDto> getBooksByAuthorName(String authorName);
     List<BookDto> getBooksByCategory(String category);
     List<BookDto> getBooksPublishedAfterYear(String year);
     List<BookDto> getAllBooksSortedByTitle();
     List<BookDto> getAllBooksSortedByAuthor();
     int getNumberOfBooks();
     double getAverageRating(Long bookId);
     int getNumberOfBooksPublishedInYear(String year);
     List<BookDto> getAvailableBooksByGenre(String category);
     void addBookToUserLibrary(Long bookId, Long userId);
     List<BookDto> getBooksInUserLibrary(Long userId);
     List<BookDto> getRecommendedBooksForUser(Long userId);
     void notifyUsersAboutNewBook(BookDto book);
     boolean addBookToCategory(String bookName, String categoryName);
     List<BannerDto> getAllBanners();
    Book getBookByTitle(String bookTitle);
    SearchRecord searchBooksByKeywordTitleOrAuthor(String key);
    List<BookDto> searchBooksByKey(String key);
    Book getBookByName(String bookName);
}
