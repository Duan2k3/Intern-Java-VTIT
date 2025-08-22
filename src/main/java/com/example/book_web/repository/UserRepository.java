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


//    @EntityGraph(attributePaths = "roles")
//    @Query("SELECT u.username FROM User u Where active = 1")
//    Optional<User> findByUsername(String username) ;

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    boolean existsByUsername(@Param("username") String username);


    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> findByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.keyActive = :keyActive and u.username = :username")
    Optional<User> findByKeyActive(@Param("keyActive") String keyActive, @Param("username") String username);


}
