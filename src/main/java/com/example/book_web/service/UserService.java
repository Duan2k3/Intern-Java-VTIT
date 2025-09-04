package com.example.book_web.service;

import com.example.book_web.dto.book.PageResponse;
import com.example.book_web.dto.token.TokenDTO;
import com.example.book_web.dto.user.UserDTO;
import com.example.book_web.entity.User;
import com.example.book_web.request.user.*;
import com.example.book_web.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {


    String login(AuthenticationRequest request) ;

     List<UserResponse> getAllUsers();
     UserDTO userDetail(String token) ;
     UserDTO createUser(UserRequest request);

     UserDTO updateUser(String token , UpdateUserRequest request);

    void deleteUser(Long id) ;

    Page<User> getUsersByKeyword(String keyword, Pageable pageable);

    String refreshToken(String token);

    String logout(String token) ;

    void ActiveUser(ActiveUserRequest request) ;

    PageResponse FilterUser(SearchUserRequest request );
}
