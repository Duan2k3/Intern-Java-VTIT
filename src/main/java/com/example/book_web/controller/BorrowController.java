package com.example.book_web.controller;

import com.example.book_web.common.ResponseConfig;
import com.example.book_web.dto.ReturnBookDTO;


import com.example.book_web.request.borrow.BorrowRequest;
import com.example.book_web.request.borrow.ReturnBookRequest;
import com.example.book_web.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BORROW')")
    public ResponseEntity<?> createBorrow(
            @Valid @RequestBody BorrowRequest request,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseConfig.success(borrowService.createBorrow(authHeader, request), "Thanh cong");

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_BORROW')")
    public ResponseEntity<?> getAllBorrows() {

        return ResponseConfig.success(borrowService.getAllBorrows(), "Thanh cong");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BORROW')")
    public ResponseEntity<?> getBorrow(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.getBorrow(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DELETE_BORROW')")
    public ResponseEntity<?> deleteBorrow(@PathVariable Long id) {
        borrowService.deleteBorrow(id);
        return ResponseConfig.success(null, "Thanh cong");
    }

    @PutMapping("/update")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_BORROW')")
    public ResponseEntity<?> updateBorrow(@RequestBody ReturnBookDTO bookDTO) {

        return ResponseConfig.success(borrowService.updateBorrow(bookDTO), "Thanh cong");

    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BORROW')")
    public ResponseEntity<?> getBorrowHistory(@RequestHeader("Authorization") String authHeader) {

        return ResponseConfig.success(borrowService.getBorrowHistory1(authHeader));
    }

    @PutMapping("/return")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CREATE_BORROW')")
    public ResponseEntity<?> returnBook(@RequestBody ReturnBookRequest request,
                                                  @RequestHeader("Authorization") String authHeader

    ) {

        borrowService.returnBook(authHeader, request);
        return ResponseConfig.success(null,"Thanh cong");

    }
}
