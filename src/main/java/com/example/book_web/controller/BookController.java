package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.Exception.CategoryNotFoundException;
import com.example.book_web.common.ResponseConfig;

import com.example.book_web.dto.book.BookDTO;
import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.dto.book.PageResponse;
import com.example.book_web.dto.store.TopBorrowedBookDto;
import com.example.book_web.entity.Book;
import com.example.book_web.request.book.BookRequest;
import com.example.book_web.request.book.SearchBookRequest;
import com.example.book_web.response.*;
import com.example.book_web.service.BookService;
import com.example.book_web.service.impl.ExcelExporter;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/books")
@Tag(name = "Book Controller")
public class BookController {
    private final BookService bookService;
    private final ExcelExporter excelExporter;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookRequest request) {
        return ResponseConfig.success(bookService.createBook(request), "Thanh cong");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_BOOK')")
    @Transactional
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return ResponseConfig.success(bookService.updateBook(id, request), "Thanh cong");


    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_BOOK')")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseConfig.success(null, "Thanh cong");

    }

    @GetMapping("/excel")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public void generate(HttpServletResponse response) throws IOException {
        excelExporter.exportBooks(response);

    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<ResponseDto<Page<Book>>> getBookByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);


        Page<Book> bookPage = bookService.getBookByKeyWord(keyword, pageable);

        return ResponseConfig.success(bookPage, "Thanh cong");

    }

    @PostMapping("/import")
    public ResponseEntity<?> importBooks(@RequestParam("file") MultipartFile file)  {
        try {
            excelExporter.importFromExcel(file);
            return ResponseEntity.ok("Import thành công!");
        } catch (CategoryNotFoundException ex) {
            return ResponseEntity.badRequest().body("Các Category ID không tồn tại : " + ex.getMissingIds());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Lỗi khi import: " + ex.getMessage());
        }
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


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<ResponseDto<BookDTO>> getById(@PathVariable Long id) {
        return ResponseConfig.success(bookService.getById(id));
    }


    @GetMapping("/export/pdf")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<?> exportBooksToPdf() throws JRException {
            byte[] pdf = bookService.exportBooksToPdf();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

    }

    @GetMapping("/top-borrowed")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<ResponseDto<List<TopBorrowedBookDto>>> getTopBorrowedBooks(
            @RequestParam(defaultValue = "0") int month,
            @RequestParam(defaultValue = "0") int year,
            @RequestParam(defaultValue = "10") int limit
    ) {

        return ResponseConfig.success(bookService.getTopBorrowedBooks(month, year, limit), "Thành công");
    }


    @PostMapping ("/search-list")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<ResponseDto<PageResponse<FilterBookDTO>>> searchProgram(
            HttpServletRequest httpServletRequest,
            @RequestBody SearchBookRequest request) {
        return ResponseConfig.success(bookService.searchBook(request), "Thanh cong");
    }

}
