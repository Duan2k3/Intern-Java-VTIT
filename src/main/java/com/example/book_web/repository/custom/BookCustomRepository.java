package com.example.book_web.repository.custom;


import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.request.book.SearchBookRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookCustomRepository  {

    Page<FilterBookDTO> searchBook(SearchBookRequest request, Pageable pageable);


}

