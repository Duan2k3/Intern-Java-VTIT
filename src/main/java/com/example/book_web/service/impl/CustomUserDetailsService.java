package com.example.book_web.service.impl;


import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageCommon messageCommon;
    @Override
    public UserDetails loadUserByUsername(String username)  {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(messageCommon.getMessage(MessageKeys.USER.USER_NOT_EXIST),"400"));
    }
}
