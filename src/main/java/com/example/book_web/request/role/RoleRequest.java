package com.example.book_web.request.role;

import com.example.book_web.entity.Role;
import lombok.Data;

import java.util.List;
@Data
public class RoleRequest {
    private Role role;
    private List<Long> userIds;
    private List<Long> permissionIds;
}
