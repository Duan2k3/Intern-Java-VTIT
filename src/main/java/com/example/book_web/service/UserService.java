package com.example.book_web.service;

import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.UserDTO;
import com.example.book_web.entity.User;
import com.example.book_web.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {


    String login(String username, String password) throws Exception;

     List<UserResponse> getAllUsers();
     UserResponse userDetail(Long id) throws Exception;
     User createUser(UserDTO userDTO);

     User updateUser(Long id , UserDTO userDTO);

    void deleteUser(Long id) throws DataNotFoundException;

    Page<User> getUsersByKeyword(String keyword, Pageable pageable);
}
