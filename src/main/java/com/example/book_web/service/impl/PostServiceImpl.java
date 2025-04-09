package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.CommentDTO;
import com.example.book_web.dto.PostDTO;
import com.example.book_web.entity.Comment;
import com.example.book_web.entity.Post;
import com.example.book_web.entity.User;
import com.example.book_web.enums.PostStatus;
import com.example.book_web.repository.PostRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.response.GetPostResponse;
import com.example.book_web.response.PostResponse;
import com.example.book_web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * @param postDTO
     * @return
     * @throws Exception
     */
    @Override
    public Post createPost(PostDTO postDTO) throws Exception {
        Optional<User> existingUser = userRepository.findById(postDTO.getUser());
        if (existingUser.isEmpty()){
            throw new  DataNotFoundException("User not existing");
        }
        User user = existingUser.get();
        Post post = Post.builder()
                .content(postDTO.getContent())
                .user(user)
                .createdAt(LocalDate.now())
                .status(PostStatus.PENDING)

                .build();
        return postRepository.save(post);
    }

    /**
     * @param id
     * @param postDTO
     * @return
     */
    @Override
    public Post updatePost(Long id, PostDTO postDTO) throws Exception {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Post not found"));

        if (!post.getUser().getId().equals(postDTO.getUser()) ) {
            throw new AccessDeniedException("Bạn không có quyền sửa bài này");
        }

        post.setContent(postDTO.getContent());
        post.setUpdatedAt(LocalDate.now());

        return postRepository.save(post);

    }

    /**
     * @param id
     */
    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);

    }

    /**
     * @param id
     * @return
     * @throws Exception
     */


    /**
     * @param postId
     * @return
     */
    @Override
    public Post approvePost(Long postId)  throws Exception{
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bài viết"));

        post.setStatus(PostStatus.APPROVED);


        return postRepository.save(post);
    }

    /**
     * @param postId
     * @return
     */
    @Override
    public Post rejectPost(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bài viết"));

        post.setStatus(PostStatus.REJECTED);

        return postRepository.save(post);
    }

    /**
     * @return
     */
    @Override
    public List<PostResponse> getPublicPosts() {
        return postRepository.findByStatus(PostStatus.APPROVED);
    }

    /**
     * @param status
     * @return
     */
    @Override
    public List<PostResponse> getPostsByStatus(PostStatus status) {

            return postRepository.findByStatus(status);

    }
    public PostDTO getPostWithComments(Long id) throws Exception {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bài viết"));

        List<GetPostResponse> rootComments = post.getComments().stream()
                .filter(c -> c.getParent() == null)
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return PostDTO.builder()

                .content(post.getContent())
                .comments(rootComments)
                .build();
    }







    public GetPostResponse convertToDto(Comment comment) {
        Optional<User> user = userRepository.findById(comment.getUserId());
        return GetPostResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userName(user.get().getFullname())
                .replies(comment.getReplies().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .build();
    }

}
