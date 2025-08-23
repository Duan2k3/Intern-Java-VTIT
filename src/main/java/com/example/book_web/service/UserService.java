package com.example.book_web.service;

import com.example.book_web.dto.token.TokenDTO;
import com.example.book_web.dto.user.UserDTO;
import com.example.book_web.entity.User;
import com.example.book_web.request.user.ActiveUserRequest;
import com.example.book_web.request.user.AuthenticationRequest;
import com.example.book_web.request.user.UserRequest;
import com.example.book_web.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {


    String login(AuthenticationRequest request) ;

     List<UserResponse> getAllUsers();
     UserResponse userDetail(Long id) ;
     UserDTO createUser(UserRequest request);

     User updateUser(Long id , UserDTO userDTO);

    void deleteUser(Long id) ;

    Page<User> getUsersByKeyword(String keyword, Pageable pageable);

    String refreshToken(String token);

    String logout(String token) ;

    void ActiveUser(ActiveUserRequest request) ;
}
