package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.entity.Post;
import com.example.book_web.entity.User;
import com.example.book_web.entity.UserLikePost;
import com.example.book_web.repository.PostRepository;
import com.example.book_web.repository.UserLikePostRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.service.UserLikePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLikePostImpl implements UserLikePostService {

    private final UserLikePostRepository likeRepo;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void likePost(Long userId, Long postId)throws Exception {
        if (likeRepo.existsByUserIdAndPostId(userId, postId)) {
            throw new RuntimeException("User already liked this post.");
        }

//        Optional<User> user = userRepository.findById(userId);
//        if (user.isEmpty()){
//            throw new DataNotFoundException("User not existing");
//        }


        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        UserLikePost like = new UserLikePost();
        like.setUserId(userId);
        like.setPost(post);
        likeRepo.save(like);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        if (!likeRepo.existsByUserIdAndPostId(userId, postId)) {
            throw new RuntimeException("User hasn't liked this post.");
        }
        likeRepo.deleteByUserIdAndPostId(userId, postId);
    }

    @Override
    public long countLikes(Long postId) {
        return likeRepo.countByPostId(postId);
    }
}
