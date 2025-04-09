package com.example.book_web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginDTO {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("password")
    private String password;
}
