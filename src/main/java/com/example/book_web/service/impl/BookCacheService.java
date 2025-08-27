package com.example.book_web.service.impl;

import com.example.book_web.entity.Book;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class BookCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "book:"; // prefix cho từng sách
    private static final String SEARCH_PREFIX = "book_search:"; // prefix cho search

    public BookCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Cache kết quả search
    public void cacheSearch(String keyword, List<Book> books) {
        String key = SEARCH_PREFIX + (keyword == null ? "all" : keyword.trim().toLowerCase());
        redisTemplate.opsForValue().set(key, books, 10, TimeUnit.MINUTES);
    }

    // Lấy kết quả search từ cache
    @SuppressWarnings("unchecked")
    public List<Book> getSearch(String keyword) {
        String key = SEARCH_PREFIX + (keyword == null ? "all" : keyword.trim().toLowerCase());
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? (List<Book>) value : null;
    }

    // Xóa cache search (ví dụ khi có update, delete sách)
    public void evictSearch(String keyword) {
        String key = SEARCH_PREFIX + (keyword == null ? "all" : keyword.trim().toLowerCase());
        redisTemplate.delete(key);
    }
}
