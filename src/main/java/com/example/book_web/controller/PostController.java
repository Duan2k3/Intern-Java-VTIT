package com.example.book_web.controller;

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
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) throws Exception{
        try {
            Post post = postService.createPost(postDTO);
            return ResponseEntity.ok("Create successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("/update/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_POST')")
    public ResponseEntity<?> updatePost( @PathVariable Long id ,@RequestBody PostDTO postDTO) throws Exception{
        try {
            Post post = postService.updatePost(id,postDTO);
            return ResponseEntity.ok("Update successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<List<PostResponse>> getPendingPosts() throws Exception{
            List<PostResponse> pendingPosts = postService.getPostsByStatus(PostStatus.PENDING);
            return ResponseEntity.ok(pendingPosts);


    }

    @GetMapping("/approve")
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<List<PostResponse>> publicPosts() throws Exception{
        List<PostResponse> pendingPosts = postService.getPublicPosts();
        return ResponseEntity.ok(pendingPosts);

    }

    @PutMapping("/approve/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_POST')")
    public ResponseEntity<?> approvePost(@PathVariable Long id) throws Exception{

            Post approvedPost = postService.approvePost(id);
            return ResponseEntity.ok("Post has approve " + id);

    }


    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<?> rejectPost(@PathVariable Long id) throws Exception {
        Post rejectedPost = postService.rejectPost(id);
        return ResponseEntity.ok("Post has reject + " + id);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_POST')")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable Long id){
        try {
            postService.deletePost(id);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Delete successfully")
                    .build());
        }

        catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                            .message(e.getMessage())
                    .build());
        }
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_POST')")
    public ResponseEntity<?> getPostWithComments(@PathVariable Long id) {
        try {
            PostDTO postDTO = postService.getPostWithComments(id);
            return ResponseEntity.ok(postDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
