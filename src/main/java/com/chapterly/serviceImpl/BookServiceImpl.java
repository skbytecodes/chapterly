package com.chapterly.serviceImpl;

import com.chapterly.aws.AmazonClient;
import com.chapterly.dto.BannerDto;
import com.chapterly.records.BannerRecord;
import com.chapterly.dto.BookDto;
import com.chapterly.entity.Author;
import com.chapterly.entity.Book;
import com.chapterly.entity.Category;
import com.chapterly.entity.Genre;
import com.chapterly.mapper.BookMapper;
import com.chapterly.records.RatingResponse;
import com.chapterly.records.SearchRecord;
import com.chapterly.repository.AuthorRepo;
import com.chapterly.repository.BookRepo;
import com.chapterly.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AmazonClient amazonClient;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private GenreService genreService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AuthorRepo authorRepo;

    Logger logger = LoggerFactory.getLogger("BookServiceImpl");


    @Value("${s3.endpointUrl}")
    private String parentUrl;


    @Transactional
    @Override
    public BookDto addBook(MultipartFile file, String data, MultipartFile authorImage) {
        BookDto response = null;
        if (data != null && file != null) {
            Book book = null;
            try {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                book = mapper.readValue(data, Book.class);
            } catch (JsonProcessingException e) {
                logger.error("ERROR ", e);
                return null;
            }

            String fileDownloadUri = "";
            String authorImageUri = "";
            try {
                fileDownloadUri = amazonClient.uploadFile(file);
                book.setImage_url(fileDownloadUri);
                book.setImageName(file.getOriginalFilename());
                if(authorImage != null){
                    Author author = authorService.getAuthorByName(book.getAuthor().getName());
                    if(author == null){
                        authorImageUri = amazonClient.uploadFile(authorImage);
                        book.getAuthor().setImageUrl(authorImageUri);
                        book.getAuthor().setImageName(authorImage.getOriginalFilename());
                    }
                }
            } catch (FileUploadException e) {
                logger.error("ERROR", e);
                return null;
            }catch (Exception e){
                logger.error("ERROR", e);
                return null;
            }
            if(book.getCategories() != null && book.getCategories().size() > 0) {
                List<Category> categories = book.getCategories().stream().map(cat -> {
                    Category category = categoryService.getCategoryByName(cat.getCategoryName());
                    if (category == null)
                        throw new RuntimeJsonMappingException("CATEGORY NOT FOUND");
                    category.setBooksCount(category.getBooksCount() + 1);
                    return category;
                }).collect(Collectors.toList());
                book.setCategories(categories);
            }

            List<Genre> genres = null;
            if(book.getAuthor() != null) {
                genres = book.getAuthor().getCategorySpecialization().stream().map(genre -> {
                    Genre genreByName = genreService.getGenreByName(genre.getName());
                    if (genreByName == null)
                        throw new RuntimeJsonMappingException("GENRE NOT FOUND");
                    return genreByName;
                }).collect(Collectors.toList());

                if (book.getAuthor().getName() != null) {
                    Author authorByName = authorService.getAuthorByName(book.getAuthor().getName());
                    if (authorByName != null)
                        book.setAuthor(authorByName);
                }
            }
            book.getAuthor().setCategorySpecialization(genres);
            Book saveBook = bookRepo.save(book);
            response = bookMapper.toDto(saveBook);
        }
        return response;
    }

    @Override
    @Cacheable(value = "bookById", key = "#p0")
    public BookDto getBookById(Long bookId) {
        BookDto bookResponse = null;
        if(bookId != null){
            Book book = bookRepo.findById(bookId).get();
            bookResponse = bookMapper.toDto(book);
            bookResponse.getAuthor().setImageUrl(parentUrl+"/"+bookResponse.getAuthor().getImageUrl());
            bookResponse.setImage_url(parentUrl+"/"+bookResponse.getImage_url());
            bookResponse.setAuthorName(book.getAuthor().getName());
            bookResponse.setAuthorName(book.getAuthor().getName());
        }
        return bookResponse;
    }

    @Override
    @CachePut(cacheNames = "updatedBook", key = "#p0")
    public BookDto updateBookDetails(Long bookId, BookDto bookDto) {
        BookDto bookResponse = null;
        if (bookId != null && bookDto != null) {
            Book book = bookRepo.findById(bookId).get();
            book.setEdition(bookDto.getEdition());
            book.setDescription(bookDto.getDescription());
            book.setISBN(bookDto.getISBN());
            book.setFormat(bookDto.getFormat());
            book.setPages(bookDto.getPages());
            book.setPrice(bookDto.getPrice());
            bookResponse = bookMapper.toDto(bookRepo.save(book));
            bookResponse.setImage_url(parentUrl+"/"+bookResponse.getImage_url());
        }
        return bookResponse;
    }

    @Override
    @CacheEvict(cacheNames = "book", key = "#p0")
    public boolean deleteBookById(Long bookId) {
        if(bookId != null){
            Book book = bookRepo.findById(bookId).get();
            if(book != null){
                bookRepo.deleteById(bookId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    @Cacheable(value = "books", key = "'cache_'+#root.methodName")
    public List<BookDto> getAllBooks() {
        List<Book> allBooks = bookRepo.findAll();
        List<BookDto> books = allBooks.stream().map(book -> {
            book.setImage_url(parentUrl+"/"+book.getImage_url());
            return bookMapper.toDto(book);
        }).collect(Collectors.toList());

        if(allBooks.size() > 0){
            return books;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "book", key = "#p0")
    public List<BookDto> searchBooksByKeyword(String title) {
        List<Book> allBooks = null;
        try {
            allBooks = bookRepo.searchBooksByTitle(title);
        }catch (Exception e){
            logger.error("ERROR", e);
        }
        List<BookDto> books = allBooks.stream().map(boo -> {
            boo.setImage_url(parentUrl+"/"+boo.getImage_url());
            BookDto book = bookMapper.toDto(boo);
            book.setAuthorName(boo.getAuthor().getName());
            return book;
        }).collect(Collectors.toList());
        if(allBooks.size() > 0){
            return books;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "bookByTitle", key = "#p0")
    public BookDto searchByTitle(String title) {
        BookDto book = null;
        try {
            Book bookByTitle = bookRepo.searchByTitle(title);
            book = bookMapper.toDto(bookByTitle);
            book.setImage_url(parentUrl+"/"+book.getImage_url());
            book.setImageName(book.getImageName());
            book.setAuthorName(bookByTitle.getAuthor().getName());
        } catch (Exception e) {
            return null;
        }
        return book;
    }

    @Override
    @Cacheable(value = "bookByAuthor", key = "#p0")
    public List<BookDto> getBooksByAuthor(Long authorId) {
        List<Book> allBooks = bookRepo.findAllBooksByAuthorId(authorId);
        List<BookDto> books = allBooks.stream().map(book -> {
            book.setImage_url(parentUrl+"/"+book.getImage_url());
            return bookMapper.toDto(book);
        }).collect(Collectors.toList());

        if(allBooks.size() > 0){
            return books;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "bookByAuthorName", key = "#p0")
    public List<BookDto> getBooksByAuthorName(String authorName) {
        List<Book> allBooks = bookRepo.findAllBooksByAuthorName(authorName);
        List<BookDto> books = allBooks.stream().map(book -> {
            book.setImage_url(parentUrl+"/"+book.getImage_url());
            return bookMapper.toDto(book);
        }).collect(Collectors.toList());

        if(allBooks.size() > 0){
            return books;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "booksByCategory", key = "#p0")
    public List<BookDto> getBooksByCategory(String category) {
        List<Book> allBooks = bookRepo.findAllBooksByCategoryName(category);
        List<BookDto> books = allBooks.stream().map(item -> {
            RatingResponse averageRating = reviewService.getAverageRatingForBook(item.getTitle());
            item.setImage_url(parentUrl+"/"+item.getImage_url());
            BookDto book = bookMapper.toDto(item);
            book.setRating(averageRating);
            return book;
        }).collect(Collectors.toList());

        if(allBooks.size() > 0){
            return books;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "booksByPublisher", key = "#p0")
    public List<BookDto> getBooksPublishedAfterYear(String year) {
        if(year != null && year.length() == 4){
            List<Book> allBooks = bookRepo.findBooksPublishedAfterYear(year);
            List<BookDto> books = allBooks.stream().map(book -> {
                book.setImage_url(parentUrl+"/"+book.getImage_url());
                return bookMapper.toDto(book);
            }).collect(Collectors.toList());

            if(allBooks.size() > 0){
                return books;
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "booksSortedByTitle", key = "'cache_'+#root.methodName")
    public List<BookDto> getAllBooksSortedByTitle() {
        List<Book> allBooks = bookRepo.findAllBooksSortedByTitle();
        List<BookDto> books = allBooks.stream().map(book -> {
            book.setImage_url(parentUrl+"/"+book.getImage_url());
            return bookMapper.toDto(book);
        }).collect(Collectors.toList());

        if(allBooks.size() > 0){
            return books;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "booksSortedByAuthor",key = "'cache_'+#root.methodName")
    public List<BookDto> getAllBooksSortedByAuthor() {
        List<Book> allBooks = bookRepo.findAllBooksSortedByAuthorName();
        List<BookDto> books = allBooks.stream().map(book -> {
            book.setImage_url(parentUrl+"/"+book.getImage_url());
            return bookMapper.toDto(book);
        }).collect(Collectors.toList());

        if(allBooks.size() > 0){
            return books;
        }
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "totalBooks", key = "'cache_'+#root.methodName")
    public int getNumberOfBooks() {
        int count = 0;
        try {
            count = bookRepo.countAllBooks();
            return count;
        }catch (Exception e){
            logger.error("ERROR", e);
        }
        return count;
    }

    @Override
    public double getAverageRating(Long bookId) {
        double averageRating = 0;
//        try {
//            averageRating = reviewService.getAverageRatingForBook(bookId);
//        }catch (Exception e){
//            logger.error("ERROR", e);
//        }
        return averageRating;
    }

    @Override
    public int getNumberOfBooksPublishedInYear(String year) {
        int count = 0;
        if (year != null && year.length() == 4) {
            try {
                count = bookRepo.findBooksPublishedInYear(year);
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return count;
    }

    @Override
    public List<BookDto> getAvailableBooksByGenre(String category) {
        return null;
    }

    @Override
    public void addBookToUserLibrary(Long bookId, Long userId) {

    }

    @Override
    public List<BookDto> getBooksInUserLibrary(Long userId) {
        return null;
    }

    @Override
    public List<BookDto> getRecommendedBooksForUser(Long userId) {
        return null;
    }

    @Override
    public void notifyUsersAboutNewBook(BookDto book) {

    }

    @Override
    public boolean addBookToCategory(String bookName, String categoryName) {
//        try {
//            bookRepo.findBookByName(bookName);
//       }catch (Exception e){
//            logger.error("ERROR", e);
//            return false;
//       }
        return false;
    }

    @Override
    public List<BannerDto> getAllBanners() {
        List<BannerRecord> booksAsForBanners = bookRepo.findBooksForBanners();
        List<BannerDto> banners = booksAsForBanners.stream().map(ban -> {
            BannerDto banner = new BannerDto();
            banner.setImageUrl(parentUrl + "/" + ban.getImage_Url());
            banner.setImageName(parentUrl + "/" + ban.getTitle());
            return banner;
        }).collect(Collectors.toList());

        if(booksAsForBanners != null)
            return banners;
        return new ArrayList<>();
    }

    @Override
    @Cacheable(value = "bookByTitle", key = "#p0")
    public Book getBookByTitle(String bookTitle) {
        try {
            Book book = bookRepo.searchByTitle(bookTitle);
            book.setImage_url(parentUrl+"/"+book.getImage_url());
            book.setImageName(book.getImageName());
            return book;
        } catch (Exception e) {
            logger.error("ERROR",e);
            return null;
        }
    }

    @Override
    @Cacheable(value = "bookSearch", key = "#p0")
    public SearchRecord searchBooksByKeywordTitleOrAuthor(String key) {
        if(key != null && !(key.equals(""))){
            List<String> result = bookRepo.searchBooksByTitleSearch(key);
            List<String> res = authorRepo.getAuthorsByName(key);
            result.addAll(res);
            return new SearchRecord(result);
        }else {
            return new SearchRecord(new ArrayList<>());
        }
    }

    @Override
    @Cacheable(value = "books", key = "#p0")
    public List<BookDto> searchBooksByKey(String key) {
        if(key != null && !(key.equals(""))){
            List<Book> result = bookRepo.searchBooksByTitle(key);
            return result.stream().map(data -> {
                data.setImage_url(parentUrl+"/"+data.getImage_url());
                return bookMapper.toDto(data);
            }).collect(Collectors.toList());
        }else {
            return new ArrayList<>();
        }
    }

    @Override
    @Cacheable(value = "bookByTitle", key = "#p0")
    public Book getBookByName(String bookTitle) {
        try {
            Book book = bookRepo.findByTitle(bookTitle);
            return book;
        } catch (Exception e) {
            logger.error("ERROR",e);
            return null;
        }
    }
}
