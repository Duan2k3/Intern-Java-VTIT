package com.example.book_web.service;

public interface UserLikePostService {
    long countLikes(Long postId);
    boolean toggleLikePost(Long userId, Long postId) ;
}
