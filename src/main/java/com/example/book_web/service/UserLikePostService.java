package com.example.book_web.service;

public interface UserLikePostService {
    void likePost(Long userId, Long postId) throws Exception;
    void unlikePost(Long userId, Long postId);
    long countLikes(Long postId);
}
