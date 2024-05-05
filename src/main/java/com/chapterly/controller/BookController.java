package com.chapterly.controller;

import com.chapterly.dto.BannerDto;
import com.chapterly.dto.BookDto;
import com.chapterly.records.SearchRecord;
import com.chapterly.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class BookController {

    @Autowired
    private BookService bookService;

    Logger logger = LoggerFactory.getLogger("BookController");


    /**
     * Adds a new book to the system.
     *
     * @param file       The file containing the book's content.
     * @param authorImage Optional file containing the author's image.
     * @param data        Additional data related to the book.
     * @return A response entity indicating the success of the operation.
     * @throws Exception If any error occurs during the process.
     */
    @PostMapping("/addBook")
    public ResponseEntity<Object> addNewBook(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "author", required = false) MultipartFile authorImage,
            String data
    ) {
        try {
            BookDto bookDto = bookService.addBook(file, data, authorImage);
            if (bookDto != null)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookById(@PathVariable("id") Long bookId) {
        if (bookId == null) {
            return new ResponseEntity<>("BOOK ID NOT PROVIDED", HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            BookDto bookById = null;
            try {
                bookById = bookService.getBookById(bookId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(bookById, HttpStatus.OK);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Long bookId, BookDto bookRequest) {
        if (bookId != null) {
            BookDto book = null;
            try {
                book = bookService.updateBookDetails(bookId, bookRequest);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long bookId) {
        if (bookId != null) {
            boolean res = false;
            try {
                res = bookService.deleteBookById(bookId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (res)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("FAILED", HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/books")
    public ResponseEntity<Object> getAllBooks() {
        try {
            List<BookDto> books = bookService.getAllBooks();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/booksByKeyword/{keyword}")
    public ResponseEntity<?> searchBooksByKeyWord(@PathVariable("keyword") String title) {
        if (title != null && title.length() > 0) {
            List<BookDto> books = null;
            try {
                books = bookService.searchBooksByKeyword(title);
            } catch (Exception e) {
                logger.error("ERROR", e);
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/bookByName/{title}")
    public ResponseEntity<?> searchByTitle(@PathVariable("title") String title) {
        if (title != null && title.length() > 0) {
            BookDto book = null;
            try {
                book = bookService.searchByTitle(title);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/booksByAuthor/{author}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable("author") Long authorId) {
        if (authorId != null) {
            List<BookDto> books = null;
            try {
                books = bookService.getBooksByAuthor(authorId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/booksByAuthorName/{authorName}")
    public ResponseEntity<?> findByAuthorName(@PathVariable("authorName") String authorName) {
        if (authorName != null && authorName.length() > 0) {
            List<BookDto> books = null;
            try {
                books = bookService.getBooksByAuthorName(authorName);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/books/category/{categoryName}")
    public ResponseEntity<?> findBooksByCategory(@PathVariable("categoryName") String category) {
        if (category != null && category.length() > 0) {
            List<BookDto> books = null;
            try {
                books = bookService.getBooksByCategory(category);
            } catch (Exception e) {
                logger.error("ERROR",e);
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/booksPublishedAfter/{year}")
    public ResponseEntity<?> getBooksPublishedAfterYear(@PathVariable("year") String year) {
        if (year != null && year.length() > 0) {
            List<BookDto> books = null;
            try {
                books = bookService.getBooksPublishedAfterYear(year);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/banners")
    public ResponseEntity<Object> getBanners(){
        try {
            List<BannerDto> banners = bookService.getAllBanners();
            return new ResponseEntity<>(banners, HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR", e);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/{key}")
    public ResponseEntity<?> searchByKeyword(@PathVariable("key") String key){
        if(key != null && key.length() > 0){
            try {
                SearchRecord result = bookService.searchBooksByKeywordTitleOrAuthor(key);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }catch (Exception e){
                logger.error("ERROR", e);
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/books/{key}")
    public ResponseEntity<?> searchBooksByKeyword(@PathVariable("key") String key){
        if(key != null && key.length() > 0){
            try {
                List<BookDto> books = bookService.searchBooksByKey(key);
                return new ResponseEntity<>(books, HttpStatus.OK);
            }catch (Exception e){
                logger.error("ERROR", e);
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
    }
}
