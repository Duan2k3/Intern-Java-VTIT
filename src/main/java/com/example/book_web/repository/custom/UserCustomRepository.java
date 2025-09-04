package com.example.book_web.repository.custom;

import com.example.book_web.dto.user.FilterUserDto;
import com.example.book_web.request.user.SearchUserRequest;
import com.example.book_web.request.user.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {

    Page<FilterUserDto> searchUser(SearchUserRequest request, Pageable pageable);
}
