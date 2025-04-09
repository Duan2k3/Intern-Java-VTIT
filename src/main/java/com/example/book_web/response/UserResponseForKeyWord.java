package com.example.book_web.response;

import com.example.book_web.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseForKeyWord {
    @JsonProperty("user_name")
    private String username;

    private String password;


    private String address;
    private String fullname;
    private String phoneNumber;
    public static UserResponseForKeyWord getUser(User user){

        UserResponseForKeyWord response = UserResponseForKeyWord.builder()
                .address(user.getAddress())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullname(user.getFullname())

                .phoneNumber(user.getPhoneNumber())

                .build();
        return response;

    }
}
