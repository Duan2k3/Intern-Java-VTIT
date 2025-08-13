package com.example.book_web.repository;

import com.example.book_web.entity.Book;
import com.example.book_web.entity.User;
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
     Optional<Book> findBookByTitle(String name);

     @Query("SELECT b FROM Book b WHERE " +
             "LOWER(b.authors) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
             "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " )
     Page<Book> findByKeyWord(@Param("keyword") String keyword, Pageable pageable);

     @Query("SELECT b.title FROM Book b")
     List<String> findAllTitles();



}
