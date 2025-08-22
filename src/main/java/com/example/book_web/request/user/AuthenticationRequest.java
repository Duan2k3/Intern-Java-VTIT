package com.example.book_web.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @JsonProperty("user_name")
    private String username;
    private String password;
}
