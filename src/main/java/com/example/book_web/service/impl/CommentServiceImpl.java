package com.example.book_web.service.impl;

import com.example.book_web.Exception.AccessDeniedHandleException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.CommentDTO;
import com.example.book_web.dto.CreateCommentDTO;
import com.example.book_web.entity.Comment;
import com.example.book_web.entity.Post;
import com.example.book_web.entity.User;
import com.example.book_web.repository.CommentRepository;
import com.example.book_web.repository.PostRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtService jwtService;

    /**
     * @param comment
     * @return
     */
    @Override
    public Comment createComment(String token ,CreateCommentDTO comment) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
        Optional<User>  user = userRepository.findByUsername(name);
        User existingUser = user.get();
        Long id = existingUser.getId();

        Optional<Post> existingPost = postRepository.findById(comment.getPostId());
        if (existingPost.isEmpty()){
            throw new DataNotFoundException("Khong tim thay bai viet","400");
        }
        Comment parentComment = null;
        if (comment.getParentId() != null) {
            parentComment = commentRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new DataNotFoundException("Comment khong ton tai","400"));

            if (!parentComment.getPost().getId().equals(comment.getPostId())) {
                throw new IllegalArgumentException("Không thể reply vào comment không thuộc cùng bài viết!");
            }
        }
        Post post = existingPost.get();
        Comment comment1 = Comment.builder()
                .content(comment.getContent())
                .createdAt(LocalDate.now())
                .post(post)
                .parent(parentComment)
                .userId(id)

                .build();
        return commentRepository.save(comment1);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Comment getCommentById(Long id)  {
       Optional<Comment> comment = commentRepository.findById(id);
       if(comment.isEmpty()){
           throw new DataNotFoundException("Cannot find comment","400");
       }
       Comment existingComment = comment.get();
       return existingComment;
    }

    /**
     * @param id
     * @param comment
     * @return
     */
    @Override
    public Comment updateComment(String token ,Long id, CommentDTO comment) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
        Optional<User>  user = userRepository.findByUsername(name);
        User existingUser = user.get();
        Long userId = existingUser.getId();

        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()){
            throw new DataNotFoundException("Cannot find Comment","400");
        }
        Comment updatecomment = existingComment.get();
        if(!updatecomment.getUserId().equals(userId)){
            throw new AccessDeniedHandleException("Cannot update comment not you","400");
        }
        updatecomment.setContent(comment.getContent());
        updatecomment.setUpdatedAt(LocalDate.now());
        return commentRepository.save(updatecomment);

    }

    /**
     * @param id
     */
    @Override
    public void deleteComment(Long id) {

        commentRepository.deleteById(id);
    }

    /**
     * @param postId
     * @return
     */
    @Override
    public List<CommentDTO> getCommentTreeByPostId(Long postId) {
        List<Comment> allComments = commentRepository.findByPostId(postId);
        List<Comment> rootComments = allComments.stream()
                .filter(c -> c.getParent() == null)
                .collect(Collectors.toList());
        return rootComments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO convertToDTO(Comment comment) {

        Optional<User> user = userRepository.findById(comment.getUserId());
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .userName(user.get().getFullname())
                .postId(comment.getPost() != null ? comment.getPost().getId() : null)
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createdAt(comment.getCreatedAt())
                .replies(comment.getReplies().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

}
