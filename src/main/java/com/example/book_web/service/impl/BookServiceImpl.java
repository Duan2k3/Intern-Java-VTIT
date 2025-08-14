package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.entity.Book;
import com.example.book_web.dto.BookDTO;
import com.example.book_web.entity.Category;
import com.example.book_web.entity.User;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.CategoryRepository;
import com.example.book_web.response.BookResponse;
import com.example.book_web.service.BookService;
import com.example.book_web.utils.MessageKeys;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final MessageCommon messageCommon;


    /**
     * @param bookDTO
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Book createBook(BookDTO bookDTO)  {
        Optional<Book> existing = bookRepository.findBookByTitle(bookDTO.getTitle());
        if (existing.isPresent()) {
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_EXISTING), "400");
        }
        List<Category> categories = new ArrayList<>();
        for (Long categoryId : bookDTO.getCategoriesIds()) {
            Optional<Category> existingCategory = categoryRepository.findById(categoryId);
            if (existingCategory.isEmpty()) {
                throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST), "400");
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

     * @param bookDTO
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Book updateBook(BookDTO bookDTO)  {
        Optional<Book> existing = bookRepository.findById(bookDTO.getId());
        if(existing.isEmpty()){
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");

        }
        Book book = existing.get();
        modelMapper.map(bookDTO,book );
        book.getCategories().clear();

        List<Category> categories = new ArrayList<>();
        for(Long categoryId : bookDTO.getCategoriesIds()){
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if(optionalCategory.isEmpty()){
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
        if(existing.isEmpty()){
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
    public Page<BookResponse> getAllBook(String keyword,  PageRequest pageRequest) {
        Page<Book> productsPage;
        productsPage = bookRepository.findByKeyWord(keyword, pageRequest);
        return productsPage.map(BookResponse::getBook);
    }

    /**
     * @return
     */






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


    public String storeBookImage(Long bookId, MultipartFile file) throws Exception{
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy sách với ID: " + bookId,"400");
        }

        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IOException("File không hợp lệ");
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

}
