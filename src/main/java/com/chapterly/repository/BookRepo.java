package com.chapterly.repository;

import com.chapterly.records.BannerRecord;
import com.chapterly.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    @Query(nativeQuery = true)
    List<Book> searchBooksByTitle(@Param("title") String title);

    @Query(nativeQuery = true)
    List<Book> findAllBooksByAuthorId(@Param("authorId") Long authorId);
    @Query(nativeQuery = true)
    List<Book> findAllBooksByAuthorName(@Param("authorName") String authorName);

    @Query(nativeQuery = true)
    List<Book> findAllBooksByCategoryName(@Param("categoryName") String categoryName);

    @Query(nativeQuery = true)
    List<Book> findBooksPublishedAfterYear(@Param("year") String year);

    @Query(nativeQuery = true)
    List<Book> findAllBooksSortedByTitle();

    @Query(nativeQuery = true)
    List<Book> findAllBooksSortedByAuthorName();

    @Query(nativeQuery = true)
    int countAllBooks();
    @Query(nativeQuery = true)
    int findBooksPublishedInYear(@Param("year") String year);

    @Query(nativeQuery = true)
    Book searchByTitle(@Param("title") String title);

    @Query(nativeQuery = true)
    Book findBookByName(String bookName);

    @Query(nativeQuery = true)
    List<BannerRecord> findBooksForBanners();

    @Query(nativeQuery = true)
    List<String> searchBooksByTitleSearch(@Param("title") String title);

    Book findByTitle(String title);
}
