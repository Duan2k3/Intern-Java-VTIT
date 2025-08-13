package com.example.book_web.controller;

import com.example.book_web.common.ResponseConfig;
import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.LoginDTO;
import com.example.book_web.dto.UserDTO;
import com.example.book_web.entity.User;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.response.*;
import com.example.book_web.service.UserService;
import com.example.book_web.service.impl.JwtService;
import com.example.book_web.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/library/user")

public class UserController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    @GetMapping("/detail/{id}")
    @Operation(summary = "get-user" , description = "Xem user")
    @PreAuthorize("hasAuthority('ROLE_VIEW_USER')")
    public ResponseEntity<?> getUserDetail(@PathVariable Long id) {
            UserResponse user = userService.userDetail(id) ;
            return ResponseConfig.success(user);

    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_USER')")
    public ResponseEntity<?> updateUser( @RequestBody UserDTO userDto) {
        return ResponseConfig.success(userService.updateUser(userDto));

    }



    @DeleteMapping("/delete/{id}")

    @PreAuthorize("hasAuthority('ROLE_DELETE_USER')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
       return ResponseConfig.success(null);


    }


    @GetMapping("/get-user")
    @PreAuthorize("hasAuthority('ROLE_VIEW_USER')")
    public ResponseEntity<UserListResponse> getUsersByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<User> userPage = userService.getUsersByKeyword(keyword, pageable);

        List<UserResponseForKeyWord> users = userPage.getContent().stream()
                .map(UserResponseForKeyWord::getUser)
                .collect(Collectors.toList());

        return ResponseEntity.ok(UserListResponse.builder()
                .users(users)
                .currentPages(userPage.getNumber())
                .totalPages(userPage.getTotalPages())
                .build());

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
       return ResponseConfig.success(userService.login(request),MessageKeys.LOGIN_SUCCESSFULLY);
        }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_USER')")
    public ResponseEntity<?> createUser(@RequestBody  UserDTO userDTO) {

           User user =  userService.createUser(userDTO);
           return ResponseConfig.success(user);

    }





}