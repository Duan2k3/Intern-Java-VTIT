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
    Borrow createBorrow(String token ,BorrowDTO borrowDTO) throws Exception;
    Borrow updateBorrow( ReturnBookDTO bookDTO) throws Exception;


    List<Borrow> getAllBorrows();
    Borrow getBorrow(Long id) throws Exception;
    void deleteBorrow(Long id) throws Exception;
//    List<BorrowDetail> getBorrowHistory(Long userId);
    List<BorrowDetail> getBorrowHistory1(String token);

    void returnBook(String token ,ReturnBookRequest request) ;
}
