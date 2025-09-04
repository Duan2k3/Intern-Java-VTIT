package com.example.book_web.service;

import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.dto.book.PageResponse;
import com.example.book_web.dto.store.TopBorrowedBookDto;
import com.example.book_web.entity.Book;
import com.example.book_web.dto.book.BookDTO;
import com.example.book_web.request.book.BookRequest;
import com.example.book_web.request.book.SearchBookRequest;
import com.example.book_web.response.BookResponse;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    BookDTO createBook(BookRequest request) ;

    BookDTO updateBook( Long id ,BookRequest request) ;
    void deleteBook(Long id) ;
    Page<BookResponse> getAllBook(String keyword  , PageRequest pageRequest);
    void generateExcel(HttpServletResponse servletResponse) ;

    Page<Book> getBookByKeyWord(String keyword , Pageable pageable);

    BookDTO getById(Long id);
    String storeBookImage(Long bookId, MultipartFile file)  throws IOException;

    byte[] exportBooksToPdf() throws JRException;

    List<TopBorrowedBookDto> getTopBorrowedBooks(int month, int year, int limit);

    PageResponse<FilterBookDTO> searchBook(SearchBookRequest request);


}
