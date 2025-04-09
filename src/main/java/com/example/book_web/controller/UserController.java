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

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_USER')")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDTO userDto) {
        userService.updateUser(id, userDto);
        return ResponseEntity.ok("User updated successfully!");
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_USER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("User deleted successfully!");
    }


    @GetMapping("/get-user")
    @PreAuthorize("hasAuthority('ROLE_VIEW_USER')")
    public ResponseEntity<UserListResponse> getUsersByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {

            Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());


            Page<User> userPage = userService.getUsersByKeyword(keyword, pageable);


            List<UserResponseForKeyWord> users = userPage.getContent().stream()
                    .map(user -> UserResponseForKeyWord.getUser(user))
                    .collect(Collectors.toList());


            return ResponseEntity.ok(UserListResponse.builder()
                    .users(users)
                    .currentPages(userPage.getNumber())
                    .totalPages(userPage.getTotalPages())
                    .build());
        }

    @PostMapping("/login")
//    @PreAuthorize("hasAuthority('ROLE_CREATE_USER')")
    public ResponseEntity<BaseResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );


            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED)));


            String token = jwtService.generateToken(user.getUsername());

            return ResponseEntity.ok(BaseResponse.builder()
                    .data(token)
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED))
                    .build())
                    ;
        }
        }



    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_USER')")
    public ResponseEntity<BaseResponse> createUser(UserDTO userDTO) throws Exception{
       try {
           User user =  userService.createUser(userDTO);
           return ResponseEntity.ok(BaseResponse.builder()
                   .data(userDTO.getUserName())
                   .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                   .build());
       }
       catch (Exception e){
           return ResponseEntity.ok(BaseResponse.builder()
                           .message(e.getMessage())
                   .build());
       }

    }





}