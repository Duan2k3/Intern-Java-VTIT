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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLikePostImpl implements UserLikePostService {

    private final UserLikePostRepository likeRepo;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean toggleLikePost(Long userId, Long postId) {
        if (likeRepo.existsByUserIdAndPostId(userId, postId)) {
            likeRepo.deleteByUserIdAndPostId(userId, postId);
            return false;
        } else {

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new DataNotFoundException("Post not found","400"));

            UserLikePost like = new UserLikePost();
            like.setUserId(userId);
            like.setPost(post);
            likeRepo.save(like);
            return true;
        }
    }

    @Override
    public long countLikes(Long postId) {
        return likeRepo.countByPostId(postId);
    }



}
