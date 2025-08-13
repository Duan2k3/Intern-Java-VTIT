package com.example.book_web.service;

import com.example.book_web.dto.BorrowDTO;
import com.example.book_web.dto.BorrowDetailDTO;
import com.example.book_web.dto.BorrowHistoryDTO;
import com.example.book_web.dto.ReturnBookDTO;
import com.example.book_web.entity.Borrow;
import com.example.book_web.entity.BorrowDetail;
import com.example.book_web.response.ReturnBookRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BorrowService {
    Borrow createBorrow(String token ,BorrowDTO borrowDTO);
    Borrow updateBorrow( ReturnBookDTO bookDTO) ;


    List<Borrow> getAllBorrows();
    Borrow getBorrow(Long id) ;
    void deleteBorrow(Long id) ;
//    List<BorrowDetail> getBorrowHistory(Long userId);
    List<BorrowDetail> getBorrowHistory1(String token);

    void returnBook(String token ,ReturnBookRequest request) ;
}
