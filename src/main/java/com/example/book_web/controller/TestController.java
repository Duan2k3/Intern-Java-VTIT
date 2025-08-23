package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.dto.TestDto;
import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.service.BookService;
import com.example.book_web.service.TestService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {


    private final TestService testServiceOld;
    private final BookService bookService;


    @GetMapping("/test-old/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BOOK')")
    public ResponseEntity<ResponseDto<List<TestDto>>> testOld(@PathVariable Long id) {
        List<TestDto> data = testServiceOld.getTestData(id);
        return ResponseConfig.success(data, "Get data success");
    }

}
