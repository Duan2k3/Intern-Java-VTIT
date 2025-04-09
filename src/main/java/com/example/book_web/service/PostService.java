package com.example.book_web.service;

import com.example.book_web.dto.PostDTO;
import com.example.book_web.entity.Post;
import com.example.book_web.enums.PostStatus;
import com.example.book_web.response.PostResponse;

import java.util.List;

public interface PostService {
    Post createPost(PostDTO postDTO) throws Exception;
    Post updatePost(Long id , PostDTO postDTO) throws Exception;
    void deletePost(Long id);
    PostDTO getPostWithComments(Long id) throws Exception;
    Post approvePost(Long postId) throws Exception;
    Post rejectPost(Long postId) throws Exception;
    List<PostResponse> getPublicPosts();
    List<PostResponse> getPostsByStatus(PostStatus status);
}
