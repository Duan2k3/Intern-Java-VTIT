package com.example.book_web.service.impl;

import com.example.book_web.entity.Book;
import com.example.book_web.response.BookResponse;
import com.example.book_web.service.BookServiceRedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceRedisImpl implements BookServiceRedis {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    private String getKeyFrom(String keyword, Long categoryId, PageRequest request) {

        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();
        Sort sort = request.getSort();
        String sortDirector = sort.getOrderFor("id")
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";

        String key = String.format("all_book:%d:%d:%s", pageNumber, pageSize, sortDirector);

        return key;
    }


    /**
     *
     */
    @Override
    public void clear() {

    }

    /**
     * @param keyword
     * @param categoryId
     * @param pageRequest
     * @return
     */
    @Override
    public List<BookResponse> getAllBook(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<BookResponse> bookResponses = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<BookResponse>>() {
        }) : null;

        return bookResponses;


    }

    /**
     * @param books
     * @param keyword
     * @param categoryId
     * @param request
     * @throws JsonProcessingException
     */
    @Override
    public void saveAllBooks(List<BookResponse> books, String keyword, Long categoryId, PageRequest request) throws JsonProcessingException {

        String key = this.getKeyFrom(keyword,categoryId,request);
        String json = redisObjectMapper.writeValueAsString(books);
        redisTemplate.opsForValue().set(key,json);


    }


}
