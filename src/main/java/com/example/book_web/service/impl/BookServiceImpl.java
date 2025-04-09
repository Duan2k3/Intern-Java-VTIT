package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.entity.Book;
import com.example.book_web.dto.BookDTO;
import com.example.book_web.entity.Category;
import com.example.book_web.entity.User;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.CategoryRepository;
import com.example.book_web.service.BookService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    /**
     * @param bookDTO
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Book createBook(BookDTO bookDTO) throws Exception {
        Optional<Book> existing = bookRepository.findBookByTitle(bookDTO.getTitle());
        if (existing.isPresent()) {
            throw new DataNotFoundException("Book already exists");
        }
        List<Category> categories = new ArrayList<>();
        for (Long categoryId : bookDTO.getCategoriesIds()) {
            Optional<Category> existingCategory = categoryRepository.findById(categoryId);
            if (existingCategory.isEmpty()) {
                throw new DataNotFoundException("Category with ID " + categoryId + " not found.");
            }
            categories.add(existingCategory.get());
        }
        Book book = Book.builder()
                .categories(categories)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .authors(bookDTO.getAuthors())
                .title(bookDTO.getTitle())
                .description(bookDTO.getDescription())
                .quantity(bookDTO.getQuantity())
                .build();
        bookRepository.save(book);

        return bookRepository.save(book);


    }

    /**
     * @param id
     * @param bookDTO
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Book updateBook(Long id, BookDTO bookDTO) throws Exception {
        Optional<Book> existing = bookRepository.findById(id);
        if(existing.isEmpty()){
            throw new Exception("Book not existing");

        }
        Book book = existing.get();
        modelMapper.map(bookDTO,book );
        book.getCategories().clear();

        List<Category> categories = new ArrayList<>();
        for(Long categoryId : bookDTO.getCategoriesIds()){
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if(optionalCategory.isEmpty()){
                throw new DataNotFoundException("Category with ID \" + categoryId + \" not found.\"");
            }
            categories.add(optionalCategory.get());
        }
        book.setUpdatedAt(LocalDate.now());
        book.setCategories(categories);
        return bookRepository.save(book);


    }

    /**
     * @param id
     */
    @Override
    @Transactional
    public void deleteBook(Long id) throws DataNotFoundException{

        Optional<Book> existing = bookRepository.findById(id);
        if(existing.isEmpty()){
            throw new DataNotFoundException("User not existing");
        }

         bookRepository.deleteById(id);
    }

    /**
     * @return
     */
    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }





    @Override
    public void generateExcel(HttpServletResponse response) throws IOException {
//        List<Book> books = bookRepository.findAll();
//        ExcelExporter excelExporter = new ExcelExporter(books);
//        excelExporter.export(response);
////        ExcelBook excelBook = new ExcelBook(books);
////        excelBook.exportBook(response);
    }

    /**
     * @param keyword
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> getBookByKeyWord(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll(pageable);
        }
        else {
            return bookRepository.findByKeyWord(keyword, pageable);

        }
    }
}
