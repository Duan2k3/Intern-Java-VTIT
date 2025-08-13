package com.example.book_web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserResponse {

    private String username;

    private String password;

    private String address;

    private String fullname;

}
