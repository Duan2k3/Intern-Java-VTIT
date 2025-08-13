package com.example.book_web.controller;

import com.example.book_web.Exception.CategoryNotFoundException;
import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.BookDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.entity.User;
import com.example.book_web.response.*;
import com.example.book_web.service.BookService;
import com.example.book_web.service.BookServiceRedis;
import com.example.book_web.service.impl.ExcelExporter;
import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/books")
@Tag(name = "Book Controller")
public class BookController {
    private final BookService bookService;
    private final LocalizationUtils localizationUtils;
    private final ExcelExporter excelExporter;
    private final BookServiceRedis productRedisService;
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    public ResponseEntity<ApiResponse> createBook(@RequestBody BookDTO bookDTO)throws  Exception{
        try {
            Book book = bookService.createBook(bookDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_BOOK))
                            .data(bookDTO)
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())

                    .build());
        }

    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_BOOK')")
    @Transactional
    public ResponseEntity<ApiResponse> updateBook( @RequestBody BookDTO bookDTO) throws Exception{
        try {
            Book book = bookService.updateBook(bookDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                            .message("Update user successfully")
                            .data(book)
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }

    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_BOOK')")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) throws Exception {
        try {

            bookService.deleteBook(id);
          return   ResponseEntity.ok(ApiResponse.builder()
                          .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BOOK))
                  .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }

    }

    @GetMapping("/excel")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public void generate(HttpServletResponse response) throws IOException{
        excelExporter.exportBooks(response);

    }


    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<SearchBook> getBookByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
//        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
        Pageable pageable = PageRequest.of(page, limit, sort);


        Page<Book> bookPage = bookService.getBookByKeyWord(keyword, pageable);

        SearchBook response = new SearchBook();
        response.setBooks(bookPage.getContent());
        response.setTotalPages(bookPage.getTotalPages());
        response.setTotalElements(bookPage.getTotalElements());
        response.setCurrentPage(bookPage.getNumber());
        return ResponseEntity.ok(response);



    }

    @PostMapping("/import")
    public ResponseEntity<?> importBooks(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            excelExporter.importFromExcel(file);
            return ResponseEntity.ok("Import thành công!");
        } catch (CategoryNotFoundException ex) {
            return ResponseEntity.badRequest().body("Các Category ID không tồn tại : " + ex.getMissingIds());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Lỗi khi import: " + ex.getMessage());
        }
    }




    @GetMapping("/redis")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<BookListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws JsonProcessingException {
        int totalPages = 0;
        //productRedisService.clear();
        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );

        List<BookResponse> productResponses = productRedisService
                .getAllBook(keyword, categoryId, pageRequest);
        if(productResponses == null) {
            Page<BookResponse> productPage = bookService
                    .getAllBook(keyword, pageRequest);

            totalPages = productPage.getTotalPages();
            productResponses = productPage.getContent();

            productRedisService.saveAllBooks(
                    productResponses,
                    keyword,
                    categoryId,
                    pageRequest
            );
        }
        if (productResponses != null) {
            System.out.println("🔥 Dữ liệu lấy từ Redis");
        } else {
            System.out.println("💾 Dữ liệu lấy từ DB vì Redis chưa có");

        }


        return ResponseEntity.ok(BookListResponse
                .builder()
                .products(productResponses)
                .totalPages(totalPages)
                .build());
    }



    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    public ResponseEntity<?> uploadBookImage(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file) {
        try {
            String fileName = bookService.storeBookImage(id, file);
            return ResponseEntity.ok("Upload thành công: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/images/{imageName}")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }







}
