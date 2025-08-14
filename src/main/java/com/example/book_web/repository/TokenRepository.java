package com.example.book_web.repository;

import com.example.book_web.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t WHERE t.refreshToken = :token AND t.isDeleted = 1")
    Optional<Token> findByRefreshToken(@Param("token") String token);

    @Query("SELECT t FROM Token t WHERE t.token = :token AND t.isDeleted = 1")
    Optional<Token> findByToken(String token);
}
