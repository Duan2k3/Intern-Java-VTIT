package com.example.book_web.dto;

import com.example.book_web.entity.Role;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RoleDTO {
    private Role role;
    private List<Long> userIds;
    private List<Long> permissionIds;
}
