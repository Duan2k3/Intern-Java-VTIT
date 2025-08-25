package com.example.book_web.repository;

import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.dto.store.TopBorrowedBookDto;
import com.example.book_web.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
     @Query("SELECT b.title FROM Book b Where b.title = :name")
     Optional<Book> findBookByTitle(@Param("name") String name);

     @Query("SELECT b FROM Book b " +
             "WHERE b.authors LIKE :keyword OR b.title LIKE :keyword")
     Page<Book> findByKeyWord(@Param("keyword") String keyword, Pageable pageable);


     @Query("SELECT b.title FROM Book b")
     List<String> findAllTitles();

     @Query("SELECT new com.example.book_web.dto.book.FilterBookDTO(b.title, b.authors, b.description, b.quantity, b.image) " +
             "FROM Book b " +
             "WHERE (:keyword IS NULL OR b.title LIKE :keyword OR b.authors LIKE :keyword)")
     Page<FilterBookDTO> filterBooks(@Param("keyword") String keyword, Pageable pageable);


     @Query("SELECT new com.example.book_web.dto.book.FilterBookDTO(b.title, b.authors, b.description, b.quantity, b.image) " +
             "FROM Book b")
     Page<FilterBookDTO> findAllBy(Pageable pageable);


     @Query(value = "CALL GetTopBorrowedBooksByMonth(:month, :year, :limit)", nativeQuery = true)
     List<TopBorrowedBookDto> getTopBorrowedBooksByMonth(
             @Param("month") int month,
             @Param("year") int year,
             @Param("limit") int limit
     );
}
