package com.example.book_web.service;

import com.example.book_web.entity.Book;
import com.example.book_web.dto.BookDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface BookService {
    Book createBook(BookDTO bookDTO) throws Exception;

    Book updateBook(Long id , BookDTO bookDTO) throws Exception;
    void deleteBook(Long id) throws Exception;
    List<Book> getAllBook();
    void generateExcel(HttpServletResponse servletResponse) throws IOException;

    Page<Book> getBookByKeyWord(String keyword , Pageable pageable);
}
