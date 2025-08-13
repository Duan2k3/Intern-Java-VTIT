package com.example.book_web.controller;

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
        try {
            UserResponse user = userService.userDetail(id) ;
            return ResponseEntity.ok(user);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @PutMapping("/update")

    @PreAuthorize("hasAuthority('ROLE_UPDATE_USER')")
    public ResponseEntity<ApiResponse> updateUser( @RequestBody UserDTO userDto) {
        userService.updateUser( userDto);
        return ResponseEntity.ok(ApiResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_USER))
                        .data(userDto)

                .build());

    }



    @DeleteMapping("/delete/{id}")

    @PreAuthorize("hasAuthority('ROLE_DELETE_USER')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_USER))
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }


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
    public ResponseEntity<ApiResponse> login(@RequestBody AuthenticationRequest request) throws Exception{
        try {
           String token =  userService.login(request);
           return ResponseEntity.ok(ApiResponse.builder()
                           .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                           .data(token)
                   .build());
        }
        catch (Exception e ){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED))
                    .build());
        }
        }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_USER')")
    public ResponseEntity<ApiResponse> createUser(UserDTO userDTO) throws Exception{
       try {
           User user =  userService.createUser(userDTO);
           return ResponseEntity.ok(ApiResponse.builder()
                   .data(userDTO)
                   .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                   .build());
       }
       catch (Exception e){
           return ResponseEntity.ok(ApiResponse.builder()
                           .message(e.getMessage())
                   .build());
       }

    }





}