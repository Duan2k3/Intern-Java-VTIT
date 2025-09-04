package com.example.book_web.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private String oldPassword;

    private String newPassword;

    private String fullName;

    private String address;

    private String phoneNumber;


}
