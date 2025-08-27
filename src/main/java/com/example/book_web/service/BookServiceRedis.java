//package com.example.book_web.service;
//
//import com.example.book_web.entity.Book;
//import com.example.book_web.response.BookResponse;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.List;
//
//public interface BookServiceRedis {
//    void clear();
//     List<BookResponse> getAllBook(String keyword , Long categoryId , PageRequest pageRequest)
//             throws JsonProcessingException;
//     void saveAllBooks(List<BookResponse> books , String keyword,
//                          Long categoryId,
//                          PageRequest request
//                          ) throws JsonProcessingException;
//}
