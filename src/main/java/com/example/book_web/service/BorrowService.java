package com.example.book_web.service;

import com.example.book_web.dto.book.PageResponse;
import com.example.book_web.dto.borrow.BorrowDTO;
import com.example.book_web.dto.borrow.ReturnBookDTO;
import com.example.book_web.dto.borrow.InforBorrowDto;
import com.example.book_web.entity.Borrow;
import com.example.book_web.entity.BorrowDetail;
import com.example.book_web.request.borrow.BorrowRequest;
import com.example.book_web.request.borrow.FilterBorrowRequest;
import com.example.book_web.request.borrow.ReturnBookRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BorrowService {
    BorrowDTO createBorrow(String token , BorrowRequest request);
    BorrowDTO updateBorrow( ReturnBookDTO bookDTO) ;

    List<Borrow> getAllBorrows();
    Borrow getBorrow(Long id) ;
    void deleteBorrow(Long id) ;
//    List<BorrowDetail> getBorrowHistory(Long userId);
    List<BorrowDetail> getBorrowHistory1(String token);

    void returnBook(String token ,ReturnBookRequest request) ;

//    List<InforBorrowDto> getInforBorrow(Long id);
//    List<InforBorrowDto> getHistoryById(String token);

    PageResponse<InforBorrowDto> getList(String token ,FilterBorrowRequest request);

}
