package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.dto.TokenDTO;
import com.example.book_web.dto.user.UserDTO;
import com.example.book_web.entity.Role;
import com.example.book_web.entity.Token;
import com.example.book_web.entity.User;
import com.example.book_web.repository.RoleRepository;
import com.example.book_web.repository.TokenRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.request.user.ActiveUserRequest;
import com.example.book_web.request.user.AuthenticationRequest;
import com.example.book_web.request.user.UserRequest;
import com.example.book_web.response.UserResponse;
import com.example.book_web.service.UserService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MessageCommon messageCommon;
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;

    @Value("${jwt.expiration}")
    private long expiration;

    @Override
    public String login(AuthenticationRequest request)  {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new DataNotFoundException(messageCommon.getMessage(MessageKeys.USER.USER_NOT_EXIST), "400"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.PASSWORD_NOT_MATCH),"400");
        }
            String token = jwtService.generateToken(user.getUsername());
        Token tokenEntity = Token.builder()
                .token(token)
                .tokenType("BEARER")
                .createdAt(LocalDateTime.now())
                .expiration(LocalDateTime.now().plusSeconds(expiration))
                .expired(null)
                .isDeleted(1)
                .refreshTokenDate(LocalDateTime.now().plusSeconds(expiration * 2))
                .userId(user.getId())
                .build();

        tokenEntity.setRefreshToken(UUID.randomUUID().toString());
        tokenRepository.save(tokenEntity);
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
             throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.USER_NOT_EXIST), "400");
           }
            return user.get();

    }

    @Override
    public UserDTO createUser(UserRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.USER.USER_IS_NULL), "400");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.USER.USER_EXISTING), "400");
        }
        Role userRole = roleRepository.findByName("USER");
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullname(request.getFullname())
                .phoneNumber(request.getPhoneNumber())
                .identityNumber(request.getIdentityNumber())
                .age(request.getAge())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .roles(List.of(userRole))
                .active(0)
                .build();
        user.setKeyActive(UUID.randomUUID().toString());

        userRepository.save(user);

        emailService.sendActiveUserNotification(user);
//        return userRepository.save(user);
   return modelMapper.map(user,UserDTO.class);

    }

    @Override
    public User updateUser(Long id ,UserDTO dto) {

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.USER_NOT_EXIST), "400");
        }
        User user = existingUser.get();
        user.setUsername(dto.getUsername());
        user.setFullname(dto.getFullname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setUpdatedAt(LocalDate.now());
        userRepository.save(user);
        return modelMapper.map(dto, User.class);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.USER_NOT_EXIST), "400");
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

    @Override
    public String refreshToken(String refreshToken) {

        Token tokenEntity = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException(
                        messageCommon.getMessage(MessageKeys.TOKEN.TOKEN_NOT_EXIST), "400"
                ));

        String name = jwtService.extractUsername(tokenEntity.getToken());
        Optional<User> user = userRepository.findByUsername(name);
        User existingUser = user.get();

        String newAccessToken = jwtService.generateToken(existingUser.getUsername());

        tokenEntity.setToken(newAccessToken);
        tokenEntity.setRefreshToken(UUID.randomUUID().toString());
        tokenEntity.setExpiration(LocalDateTime.now().plusSeconds(expiration));
        tokenEntity.setRefreshTokenDate(LocalDateTime.now().plusSeconds(expiration * 2));
        tokenEntity.setExpired(null);
        tokenEntity.setUpdatedAt(LocalDateTime.now());
        tokenRepository.save(tokenEntity);

        return newAccessToken;
    }

    /**
     * @param token
     * @return
     */
    @Override
    public String logout(TokenDTO token) {
        Token existingToken = tokenRepository.findByRefreshToken(token.getRefreshToken())
                .orElseThrow(() -> new DataNotFoundException(
                        messageCommon.getMessage(MessageKeys.TOKEN.TOKEN_NOT_EXIST), "400"
                ));

        existingToken.setExpired(LocalDateTime.now());
        existingToken.setIsDeleted(0);
        tokenRepository.save(existingToken);
        return messageCommon.getMessage(MessageKeys.TOKEN.LOGOUT_SUCCESS);
    }


    @Override
    public void ActiveUser(ActiveUserRequest request) {
        Optional<User> user = userRepository.findByKeyActive(request.getKeyActive(), request.getUsername());
        if (user.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.USER_NOT_EXIST), "400");
        }
        User existingUser = user.get();
        existingUser.setActive(1);
        existingUser.setKeyActive(UUID.randomUUID().toString());
        userRepository.save(existingUser);

    }

}