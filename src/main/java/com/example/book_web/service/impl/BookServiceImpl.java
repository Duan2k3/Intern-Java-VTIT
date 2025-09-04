package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.Exception.FileProcessException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.dto.book.BookPageCacheDTO;
import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.dto.book.PageResponse;
import com.example.book_web.dto.store.TopBorrowedBookDto;
import com.example.book_web.entity.Book;
import com.example.book_web.dto.book.BookDTO;
import com.example.book_web.entity.Borrow;
import com.example.book_web.entity.Category;
import com.example.book_web.entity.NotificationEmail;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.BorrowRepository;
import com.example.book_web.repository.CategoryRepository;
import com.example.book_web.repository.custom.BookCustomRepository;
import com.example.book_web.request.book.BookRequest;
import com.example.book_web.request.book.SearchBookRequest;
import com.example.book_web.response.BookResponse;
import com.example.book_web.service.BookService;
import com.example.book_web.utils.MessageKeys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final MessageCommon messageCommon;
    private final BookCustomRepository bookCustomRepository;
    private final BorrowRepository borrowRepository;


    private static final String BOOK_CACHE_KEY = "books";
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public BookDTO createBook(BookRequest request) {
        log.info("Creating book with title: {}", request.getTitle());

        Optional<Book> existingBook = bookRepository.findBookByTitle(request.getTitle());
        if (existingBook.isPresent()){
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_EXISTING),"400");
        }

        if (request.getQuantity() <= 0) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.QUANTITY_NOT_VALID), "400");
        }

        List<Category> categories = categoryRepository.findAllByIds(request.getCategoriesIds());

        if (categories.size() != request.getCategoriesIds().size()) {
            throw new DataNotFoundException(
                    messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST), "400"
            );
        }

        Book book = Book.builder()
                .categories(categories)
                .createdAt(LocalDate.now())
                .authors(request.getAuthors())
                .title(request.getTitle())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with ID: {}", savedBook.getId());

        clearBookCache();
        return modelMapper.map(savedBook, BookDTO.class);
    }

    /**
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookRequest request) {
        log.info("Updating book with ID: {}", id);
        Optional<Book> existing = bookRepository.findById(id);
        if (existing.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");
        }
        Optional<Book> bookExisting = bookRepository.findBookByTitle(request.getTitle());
        if (bookExisting.isPresent() && !bookExisting.get().getId().equals(id)) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_EXISTING), "400");
        }
        Book book = existing.get();
        modelMapper.map(request, book);
        book.getCategories().clear();
        List<Category> categories = categoryRepository.findAllByIds(request.getCategoriesIds());
        if (categories.size() != request.getCategoriesIds().size()) {
            throw new DataNotFoundException(
                    messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST), "400"
            );
        }
        book.setUpdatedAt(LocalDate.now());
        book.setCategories(categories);
        log.info("Book updated successfully with ID: {}", book.getId());
        clearBookCache();
        bookRepository.save(book);
        return modelMapper.map(book,BookDTO.class);

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
        borrowRepository.deleteByBorrowId(id);
        log.info("Deleting book with ID: {}", id);
        bookRepository.deleteById(id);
        clearBookCache();

    }

    /**
     * @param keyword
     * @param pageRequest
     * @return
     */
    @Override
    public Page<BookResponse> getAllBook(String keyword, PageRequest pageRequest) {
        log.info("Fetching books with keyword: {}", keyword);
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
        log.info("Searching books with keyword: {}", keyword);
        String cacheKey = generateCacheKey(BOOK_CACHE_KEY, keyword, pageable);
        // 1. Kiểm tra cache
        BookPageCacheDTO cachedPage = (BookPageCacheDTO) redisTemplate.opsForValue().get(cacheKey);
        // 2. Nếu cache hit -> trả về kết quả từ cache
        if (cachedPage != null) {
            log.info("Cache hit for key: {}", cacheKey);
            return new PageImpl<>(
                    cachedPage.getContent().stream()
                            .map(bookDTO -> modelMapper.map(bookDTO, Book.class))
                            .collect(Collectors.toList()),
                    pageable,
                    cachedPage.getTotalElements()
            );
        }

        // 3. Nếu cache miss -> query DB
        Page<Book> bookPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            bookPage = bookRepository.findAll(pageable);
        } else {
            bookPage = bookRepository.findByKeyWord("%" + keyword.trim() + "%", pageable);
        }
        // 4. Lưu cache
        BookPageCacheDTO cacheDTO = new BookPageCacheDTO(
                bookPage.getContent().stream()
                        .map(book -> modelMapper.map(book, BookDTO.class))
                        .collect(Collectors.toList()),
                bookPage.getTotalElements(),
                bookPage.getTotalPages()
        );

        redisTemplate.opsForValue().set(cacheKey, cacheDTO, 30, TimeUnit.MINUTES);
        return bookPage;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public BookDTO getById(Long id) {
        log.info("Fetching book with ID: {}", id);
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");
        }
        Book existingBook = book.get();
        BookDTO bookDTO = modelMapper.map(existingBook, BookDTO.class);
        return bookDTO;
    }

    public String storeBookImage(Long bookId, MultipartFile file) throws IOException{
        log.info("Storing image for book ID: {}", bookId);
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");
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
        log.info("Exporting books to PDF report");
        List<Book> books = bookRepository.findAll();

        List<Map<String, Object>> data = books.stream().map(b -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", b.getTitle());
            map.put("description", b.getDescription());
            map.put("quantity", b.getQuantity());
            map.put("createdAt", java.sql.Date.valueOf(b.getCreatedAt()));
            return map;
        }).collect(Collectors.toList());
        // Load file jrxml từ resources
        InputStream reportStream = getClass().getResourceAsStream("/reports/Book.jrxml");
        if (reportStream == null) {
            throw new DataNotFoundException("Khong tim thay mau export","400");
        }

        // Compile report
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);


        // Data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        Map<String, Object> parameters = new HashMap<>();

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export ra byte[]
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    /**
     * @param month
     * @param year
     * @param limit
     * @return
     */
    @Override
    public List<TopBorrowedBookDto> getTopBorrowedBooks(int month, int year, int limit) {
        log.info("Fetching top {} borrowed books for {}/{}", limit, month, year);
        return bookRepository.getTopBorrowedBooksByMonth(month, year, limit);
    }

    /**
     * @param request
     * @return
     */
    @Override
    public PageResponse<FilterBookDTO> searchBook(SearchBookRequest request) {
        int pageNumber = request.getPageNumber() != null && request.getPageNumber() > 0
                ? request.getPageNumber() - 1
                : 0;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        String cacheKey = generateCacheKey(BOOK_CACHE_KEY, request.toString(), pageable);

        // 1. Kiểm tra trong Redis
        PageResponse<FilterBookDTO> cachedResponse =
                (PageResponse<FilterBookDTO>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedResponse != null) {
            log.info("Cache hit for searchBook with key: {}", cacheKey);
            return cachedResponse;
        }

        // 2. Nếu cache miss -> query DB
        Page<FilterBookDTO> page = bookCustomRepository.searchBook(request, pageable);

        PageResponse<FilterBookDTO> response = new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent()
        );

        // 3. Lưu vào Redis
        redisTemplate.opsForValue().set(cacheKey, response, 30, TimeUnit.MINUTES);
        log.info("Cache stored for searchBook with key: {}", cacheKey);

        return response;
    }


    private String generateCacheKey(String prefix, String keyword, Pageable pageable) {
        String sortField = pageable.getSort().isEmpty()
                ? "id"
                : pageable.getSort().iterator().next().getProperty();

        String sortDir = pageable.getSort().isEmpty()
                ? "asc"
                : pageable.getSort().iterator().next().getDirection().name().toLowerCase();

        String rawKey = (keyword == null ? "all" : keyword.trim())
                + "::" + pageable.getPageNumber()
                + "::" + pageable.getPageSize()
                + "::" + sortField
                + "::" + sortDir;

        String hash = DigestUtils.md5DigestAsHex(rawKey.getBytes());
        return prefix + "::" + hash;
    }


    private void clearBookCache() {
        Set<String> keys = redisTemplate.keys(BOOK_CACHE_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Cleared {} book cache entries", keys.size());
        } else {
            log.info("No book cache entries found to clear");
        }
    }

}



