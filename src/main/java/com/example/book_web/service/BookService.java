package com.example.book_web.service;

import com.example.book_web.entity.Book;
import com.example.book_web.dto.BookDTO;
import com.example.book_web.response.BookResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    Book createBook(BookDTO bookDTO) ;

    Book updateBook( Long id ,BookDTO bookDTO) ;
    void deleteBook(Long id) ;
    Page<BookResponse> getAllBook(String keyword  , PageRequest pageRequest);
    void generateExcel(HttpServletResponse servletResponse) throws IOException;

    Page<Book> getBookByKeyWord(String keyword , Pageable pageable);

    BookDTO getById(Long id);
    String storeBookImage(Long bookId, MultipartFile file) throws Exception;
}
