package com.example.book_web.dto.permission;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class PermissionDTO {

    private String name;

    private String description;


}
