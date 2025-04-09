package com.example.book_web.service;

import com.example.book_web.dto.BorrowDTO;
import com.example.book_web.dto.ReturnBookDTO;
import com.example.book_web.entity.Borrow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BorrowService {
    Borrow createBorrow(BorrowDTO borrowDTO) throws Exception;
    Borrow updateBorrow( Long id ,ReturnBookDTO bookDTO) throws Exception;


    List<Borrow> getAllBorrows();
    Borrow getBorrow(Long id) throws Exception;
    void deleteBorrow(Long id) throws Exception;
}
