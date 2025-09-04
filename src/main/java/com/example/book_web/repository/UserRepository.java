package com.example.book_web.repository;
import com.example.book_web.entity.User;
import com.example.book_web.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT new com.example.book_web.response.UserResponse(u.username," +
            " u.password, u.address, " +
            "u.fullname) " +
            "FROM User u")
    List<UserResponse> findAllUsers();

    @Query("SELECT new com.example.book_web.response.UserResponse(u.username, " +
            "u.password, u.address, " +
            "u.fullname)" +
            " FROM User u WHERE u.id = :id")
    Optional<UserResponse> findUserById(@Param("id") Long id);


    @EntityGraph(attributePaths = "roles")
    @Query("SELECT u FROM User u Where u.active = 1 and u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    boolean existsByUsername(@Param("username") String username);


    @Query("SELECT u FROM User u " +
            "WHERE (:keyword IS NULL OR :keyword = '' " +
            "   OR u.username LIKE %:keyword% " +
            "   OR u.fullname LIKE %:keyword% " +
            "   OR u.address LIKE %:keyword%)")
    Page<User> findByKeyWord(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT u FROM User u WHERE u.keyActive = :keyActive and u.username = :username")
    Optional<User> findByKeyActive(@Param("keyActive") String keyActive, @Param("username") String username);


}
