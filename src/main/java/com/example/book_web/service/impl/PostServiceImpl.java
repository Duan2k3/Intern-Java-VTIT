package com.example.book_web.service.impl;

import com.example.book_web.Exception.AccessDeniedHandleException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.post.PostDTO;
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
    public Post createPost(PostDTO postDTO)  {
        Optional<User> existingUser = userRepository.findById(postDTO.getUser());
        if (existingUser.isEmpty()){
            throw new  DataNotFoundException("User not existing","400");
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
    public Post updatePost(Long id, PostDTO postDTO){

        Optional<Post> post = postRepository.findById(id);

        if (!post.get().getUser().getId().equals(postDTO.getUser()) ) {
            throw new AccessDeniedHandleException("Bạn không có quyền sửa bài này","400");
        }

        post.get().setContent(postDTO.getContent());
        post.get().setUpdatedAt(LocalDate.now());

        Post existingPost = post.get();
        return postRepository.save(existingPost);

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
    public Post approvePost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bài viết","400"));

        post.setStatus(PostStatus.APPROVED);


        return postRepository.save(post);
    }

    /**
     * @param postId
     * @return
     */
    @Override
    public Post rejectPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bài viết","400"));

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
    public PostDTO getPostWithComments(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bài viết","400"));

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
