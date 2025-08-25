package com.example.book_web.dto.user;

import com.example.book_web.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UserDTO {

    private String username;

    private String email;

    private String fullname;

    private String phoneNumber;

    private String identityNumber;

    private Long age;

    private LocalDate DateOfBirth;

    private String address;

}
