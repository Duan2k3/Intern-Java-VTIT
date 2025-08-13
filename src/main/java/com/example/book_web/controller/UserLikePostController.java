package com.example.book_web.controller;

import com.example.book_web.response.BaseResponse;
import com.example.book_web.response.LikeResponse;
import com.example.book_web.service.UserLikePostService;
import com.example.book_web.service.impl.UserLikePostImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/like")
public class UserLikePostController {

    private final UserLikePostService userLikePostService;


    @GetMapping("/{id}")
    public ResponseEntity<?> countLikes(@PathVariable Long id) {
        long likeCount = userLikePostService.countLikes(id);
        Map<String, Object> response = new HashMap<>();
        response.put("postId", id);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dislike")
    public ResponseEntity<BaseResponse> likeanddislike(@RequestBody LikeResponse likeResponse) throws Exception{

        try {
            boolean isLiked = userLikePostService.toggleLikePost(likeResponse.getUserId(), likeResponse.getPostId());

            String message = isLiked ? "Đã thích bài viết" : "Đã bỏ thích bài viết";

            long sum = userLikePostService.countLikes(likeResponse.getPostId());
            return ResponseEntity.ok(BaseResponse.builder()
                              .data(String.valueOf(sum))
                            .message(message)

                    .build());

        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                            .message(e.getMessage())
                    .build());
        }


    }
}
