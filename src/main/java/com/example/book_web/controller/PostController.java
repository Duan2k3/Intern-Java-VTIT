package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.dto.PostDTO;
import com.example.book_web.entity.Post;
import com.example.book_web.enums.PostStatus;
import com.example.book_web.response.BaseResponse;
import com.example.book_web.response.PostResponse;
import com.example.book_web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/post")
public class PostController {
    private final PostService postService;
    @PostMapping("/create")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) {
            return ResponseConfig.success(postService.createPost(postDTO),"Thanh cong");

    }
    @PutMapping("/update/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_POST')")
    public ResponseEntity<?> updatePost( @PathVariable Long id ,@RequestBody PostDTO postDTO) {
            return ResponseConfig.success(postService.updatePost(id,postDTO),"Thanh cong");

    }
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<ResponseDto<List<PostResponse>>> getPendingPosts(){
        return ResponseConfig.success(postService.getPostsByStatus(PostStatus.PENDING),"Thanh cong");
    }

    @GetMapping("/approve")
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<ResponseDto<List<PostResponse>>> publicPosts(){
        List<PostResponse> pendingPosts = postService.getPublicPosts();
        return ResponseConfig.success(postService.getPublicPosts());

    }

    @PutMapping("/approve/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_POST')")
    public ResponseEntity<?> approvePost(@PathVariable Long id){
            return ResponseConfig.success(postService.approvePost(id),"Thanh cong");

    }


    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<?> rejectPost(@PathVariable Long id){
        return ResponseConfig.success(postService.rejectPost(id),"Thanh cong");
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_POST')")
    public ResponseEntity<?> deletePost(@PathVariable Long id){

            postService.deletePost(id);
            return ResponseConfig.success(null,"Thanh cong");

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_POST')")
    public ResponseEntity<?> getPostWithComments(@PathVariable Long id) {
            return ResponseConfig.success(postService.getPostWithComments(id),"Thanh cong");

    }
}
