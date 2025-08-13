package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.UserDTO;
import com.example.book_web.entity.Role;
import com.example.book_web.entity.User;
import com.example.book_web.repository.RoleRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.response.AuthenticationRequest;
import com.example.book_web.response.BaseResponse;
import com.example.book_web.response.UserResponse;
import com.example.book_web.service.UserService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final LocalizationUtils localizationUtils;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public String login(AuthenticationRequest request) throws Exception {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );


            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
        }


            String token = jwtService.generateToken(user.getUsername());

            return token;


    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public UserResponse userDetail(Long id) {

           Optional<UserResponse> user = userRepository.findUserById(id);
           if (user.isEmpty()){
             throw new DataNotFoundException("Khong tim thay user","400");
           }
            return user.get();

    }

    @Override
    public User createUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUserName())) {
            throw new DataExistingException("Tên người dùng đã tồn tại","400");
        }
        Role userRole = roleRepository.findByName("USER");
        User user = User.builder()
                .username(dto.getUserName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullname(dto.getFullname())
                .phoneNumber(dto.getPhoneNumber())
                .identityNumber(dto.getIdentityNumber())
                .age(dto.getAge())
                .email(dto.getEmail())
                .dateOfBirth(dto.getBirthday())
                .address(dto.getAddress())
                .roles(List.of(userRole))
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDTO dto) {

        Optional<User> existingUser = userRepository.findById(dto.getId());

        User user = existingUser.get();
        user.setFullname(dto.getFullname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setUpdatedAt(LocalDate.now());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        }
        userRepository.deleteById(id);
    }

    /**
     * @param keyword
     * @param pageable
     * @return
     */
    @Override
    public Page<User> getUsersByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findByKeyWord(keyword, pageable);
        }
    }
}