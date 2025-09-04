package com.example.book_web.repository.custom;

import com.example.book_web.dto.borrow.InforBorrowDto;
import com.example.book_web.request.borrow.FilterBorrowRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowCustomRepository {

    Page<InforBorrowDto> listBorrowHistory(Long id ,FilterBorrowRequest request , Pageable pageable);
}
