package com.example.book_web.repository;

import com.example.book_web.entity.Post;
import com.example.book_web.enums.PostStatus;
import com.example.book_web.response.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT new com.example.book_web.response.PostResponse(p.id," +
            " p.content, " +
            "p.user.fullname, " +
            "p.status) " +
            "FROM Post p WHERE p.status = :status")
    List<PostResponse> findByStatus(@Param("status") PostStatus status);



}
