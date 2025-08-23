package com.example.book_web.service;

import com.example.book_web.dto.post.PostDTO;
import com.example.book_web.entity.Post;
import com.example.book_web.enums.PostStatus;
import com.example.book_web.response.PostResponse;

import java.util.List;

public interface PostService {
    Post createPost(PostDTO postDTO) ;
    Post updatePost(Long id , PostDTO postDTO) ;
    void deletePost(Long id);
    PostDTO getPostWithComments(Long id) ;
    Post approvePost(Long postId) ;
    Post rejectPost(Long postId) ;
    List<PostResponse> getPublicPosts();
    List<PostResponse> getPostsByStatus(PostStatus status);
}
