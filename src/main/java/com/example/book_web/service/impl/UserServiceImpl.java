package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.UserDTO;
import com.example.book_web.entity.Role;
import com.example.book_web.entity.User;
import com.example.book_web.repository.RoleRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.response.UserResponse;
import com.example.book_web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Override
    public String login(String username, String password) throws Exception {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy user");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadCredentialsException("Mật khẩu không đúng");
        }
        return username;
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
        try {
           Optional<UserResponse> user = userRepository.findUserById(id);
            return user.get();
        }
        catch (Exception e){
            throw new RuntimeException("Lỗi khi lấy thông tin user: " + e.getMessage());
        }

    }

    @Override
    public User createUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUserName())) {
            throw new RuntimeException("Tên người dùng đã tồn tại");
        }
        Role userRole = roleRepository.findByName("USER");
        User user = User.builder()
                .username(dto.getUserName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullname(dto.getFullname())
                .phoneNumber(dto.getPhoneNumber())
                .identityNumber(dto.getIdentityNumber())
                .age(dto.getAge())
                .dateOfBirth(dto.getBirthday())
                .address(dto.getAddress())
                .roles(List.of(userRole))
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        user.setFullname(dto.getFullname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setUpdatedAt(LocalDate.now());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy user");
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