package com.example.book_web.controller;

import com.example.book_web.dto.BorrowDTO;
import com.example.book_web.dto.ReturnBookDTO;
import com.example.book_web.entity.Borrow;
import com.example.book_web.service.BorrowService;
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
    public ResponseEntity<?> createBorrow(@RequestBody BorrowDTO borrowDTO) throws Exception{
       try {
         Borrow borrow =  borrowService.createBorrow(borrowDTO);
           return ResponseEntity.ok("Them thanh cong");
       }
       catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }

    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_BORROW')")
    public ResponseEntity<?> getAllBorrows() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BORROW')")
    public ResponseEntity<?> getBorrow(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(borrowService.getBorrow(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_BORROW')")
    public ResponseEntity<?> deleteBorrow(@PathVariable Long id) throws Exception {
        borrowService.deleteBorrow(id);
        return ResponseEntity.ok("Deleted borrow successfully");
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_BORROW')")
    public ResponseEntity<?> updateBorrow(@PathVariable Long id , @RequestBody ReturnBookDTO bookDTO){
        try {
            borrowService.updateBorrow(id,bookDTO);
            return ResponseEntity.ok("Update thanh cong");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
