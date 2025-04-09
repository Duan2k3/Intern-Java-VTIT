package com.example.book_web.controller;

import com.example.book_web.response.LikeResponse;
import com.example.book_web.service.impl.UserLikePostImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/like")
public class UserLikePostController {

    private final UserLikePostImpl userLikePostService;

    @PostMapping("like-post")
    public ResponseEntity<?> likePost(@RequestBody LikeResponse likeResponse) throws Exception{
        userLikePostService.likePost(likeResponse.getUserId(), likeResponse.getPostId());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Liked post successfully!");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> unlikePost(@RequestParam Long userId, @RequestParam Long postId) {
        userLikePostService.unlikePost(userId, postId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Unliked post successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> countLikes(@RequestParam Long postId) {
        long likeCount = userLikePostService.countLikes(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }
}
