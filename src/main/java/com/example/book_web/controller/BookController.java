package com.example.book_web.controller;

import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.BookDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.entity.User;
import com.example.book_web.response.BaseResponse;
import com.example.book_web.response.SearchBook;
import com.example.book_web.response.UserListResponse;
import com.example.book_web.response.UserResponseForKeyWord;
import com.example.book_web.service.BookService;
import com.example.book_web.service.impl.ExcelExporter;
import com.example.book_web.utils.MessageKeys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/books")
public class BookController {
    private final BookService bookService;
    private final LocalizationUtils localizationUtils;
    private final ExcelExporter excelExporter;
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO)throws  Exception{
        try {
            Book book = bookService.createBook(bookDTO);
            return ResponseEntity.ok("Create book successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_BOOK')")
    public ResponseEntity<BaseResponse> updateBook(@PathVariable Long id , @RequestBody BookDTO bookDTO) throws Exception{
        try {
            Book book = bookService.updateBook(id,bookDTO);
            return ResponseEntity.ok(BaseResponse.builder()
                            .message("Update user successfully")
                            .data(bookDTO.toString())
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                            .message(e.getMessage())
                    .build());
        }

    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_BOOK')")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) throws Exception {
        try {

            bookService.deleteBook(id);
          return   ResponseEntity.ok("Delete book success fully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/excel")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public void generate(HttpServletResponse response) throws IOException{
        excelExporter.exportBooks(response);

    }


    @GetMapping("/import-excel")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    @Transactional
    public ResponseEntity<?> importBook(@RequestParam("file") MultipartFile file) throws IOException{

        try {
            excelExporter.ImportToExcel(file);
            return ResponseEntity.ok("Import file thanh cong");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    @GetMapping("/search")
    public ResponseEntity<SearchBook> getBookByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());


        Page<Book> bookPage = bookService.getBookByKeyWord(keyword, pageable);

        SearchBook response = new SearchBook();
        response.setBooks(bookPage.getContent());
        response.setTotalPages(bookPage.getTotalPages());
        response.setTotalElements(bookPage.getTotalElements());
        response.setCurrentPage(bookPage.getNumber());
        return ResponseEntity.ok(response);



    }





}
