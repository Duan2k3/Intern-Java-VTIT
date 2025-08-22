package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.Exception.FileProcessException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.dto.book.BookDTO;
import com.example.book_web.entity.Category;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.CategoryRepository;
import com.example.book_web.request.book.BookRequest;
import com.example.book_web.response.BookResponse;
import com.example.book_web.service.BookService;
import com.example.book_web.utils.MessageKeys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final MessageCommon messageCommon;


    /**
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public BookDTO createBook(BookRequest request) {
        Optional<Book> existing = bookRepository.findBookByTitle(request.getTitle());
        if (existing.isPresent()) {
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_EXISTING), "400");
        }
        if (request.getQuantity() <= 0) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.QUANTITY_NOT_VALID), "400");
        }
        List<Category> categories = new ArrayList<>();
        for (Long categoryId : request.getCategoriesIds()) {
            Optional<Category> existingCategory = categoryRepository.findById(categoryId);
            if (existingCategory.isEmpty()) {
                throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST), "400");
            }
            categories.add(existingCategory.get());
        }
        Book book = Book.builder()
                .categories(categories)
                .createdAt(LocalDate.now())
                .authors(request.getAuthors())
                .title(request.getTitle())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .build();
       Book saveBook =  bookRepository.save(book);

        return modelMapper.map(saveBook, BookDTO.class);


    }

    /**
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Book updateBook(Long id, BookRequest request) {
        Optional<Book> existing = bookRepository.findById(id);
        if (existing.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");

        }

        Optional<Book> bookExisting = bookRepository.findBookByTitle(request.getTitle());
        if (bookExisting.isPresent()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_EXISTING), "400");
        }
        Book book = existing.get();
        modelMapper.map(request, book);
        book.getCategories().clear();

        List<Category> categories = new ArrayList<>();
        for (Long categoryId : request.getCategoriesIds()) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalCategory.isEmpty()) {
                throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST) + categoryId, "400");
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
    public void deleteBook(Long id) {

        Optional<Book> existing = bookRepository.findById(id);
        if (existing.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");
        }

        bookRepository.deleteById(id);
    }

    /**
     * @param keyword
     * @param pageRequest
     * @return
     */
    @Override
    public Page<BookResponse> getAllBook(String keyword, PageRequest pageRequest) {
        Page<Book> productsPage;
        productsPage = bookRepository.findByKeyWord(keyword, pageRequest);
        return productsPage.map(BookResponse::getBook);
    }

    /**
     * @return
     */


    @Override
    public void generateExcel(HttpServletResponse response)  {
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
        } else {

            Page<Book> bookPage = bookRepository.findByKeyWord(keyword, pageable);
            bookPage.getTotalPages();
            bookPage.getTotalElements();
            bookPage.getNumberOfElements();
            bookPage.getSize();

            return bookPage;
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public BookDTO getById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");
        }
        Book existingBook = book.get();
        BookDTO bookDTO = modelMapper.map(existingBook, BookDTO.class);
        return bookDTO;
    }


    public String storeBookImage(Long bookId, MultipartFile file) throws IOException{
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy sách với ID: " + bookId, "400");
        }

        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new FileProcessException("File không hợp lệ", "400");
        }


        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");


        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);


        Book book = optionalBook.get();
        book.setImage(fileName);
        bookRepository.save(book);

        return fileName;

    }


    /**
     * @return
     * @throws JRException
     */
    @Override
    public byte[] exportBooksToPdf() throws JRException{
        List<Book> books = bookRepository.findAll();

        // Load file jrxml từ resources
        InputStream reportStream = getClass().getResourceAsStream("/reports/Book.jrxml");
        if (reportStream == null) {
            throw new RuntimeException("Không tìm thấy file reports/Book.jrxml");
        }

        // Compile report
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);


        // Data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(books);

        Map<String, Object> parameters = new HashMap<>();

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export ra byte[]
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }




    }



