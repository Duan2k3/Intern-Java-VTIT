package com.example.book_web.controller;

import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.BorrowDTO;
import com.example.book_web.dto.BorrowDetailDTO;
import com.example.book_web.dto.BorrowHistoryDTO;
import com.example.book_web.dto.ReturnBookDTO;
import com.example.book_web.entity.Borrow;
import com.example.book_web.entity.BorrowDetail;
import com.example.book_web.response.ApiResponse;

import com.example.book_web.response.ReturnBookRequest;
import com.example.book_web.service.BorrowService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/borrow")
public class BorrowController {
    private final BorrowService borrowService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BORROW')")
    public ResponseEntity<ApiResponse> createBorrow(
            @RequestBody BorrowDTO borrowDTO,
            @RequestHeader("Authorization") String authHeader) throws Exception{
       try {
         Borrow borrow =  borrowService.createBorrow(authHeader,borrowDTO);
           return ResponseEntity.ok(ApiResponse.builder()
                           .message(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_BORROW))
                           .data(borrow)

                   .build());
       }
       catch (Exception e){
           return ResponseEntity.badRequest().body(ApiResponse.builder()
                           .message(e.getMessage())

                   .build());
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
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DELETE_BORROW')")
    public ResponseEntity<ApiResponse> deleteBorrow(@PathVariable Long id) throws Exception {
        borrowService.deleteBorrow(id);
        return ResponseEntity.ok(ApiResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BORROW))
                .build());
    }
    @PutMapping("/update")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_BORROW')")
    public ResponseEntity<ApiResponse> updateBorrow( @RequestBody ReturnBookDTO bookDTO){
        try {
            borrowService.updateBorrow(bookDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_BORROW))
                            .data(bookDTO)
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BORROW')")
    public ResponseEntity<?> getBorrowHistory(@RequestHeader("Authorization") String authHeader) throws Exception{

       try {
           return ResponseEntity.ok( borrowService.getBorrowHistory1(authHeader));

       }
       catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }


    @PutMapping("/return")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CREATE_BORROW')")
    public ResponseEntity<ApiResponse> returnBook(@RequestBody ReturnBookRequest request,
                                                   @RequestHeader("Authorization") String authHeader

    )  throws Exception {
        try {
            borrowService.returnBook(authHeader, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Tra sach thanh cong")
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }

    }

}
