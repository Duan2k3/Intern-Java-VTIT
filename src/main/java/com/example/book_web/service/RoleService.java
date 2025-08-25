package com.example.book_web.service;

import com.example.book_web.dto.role.RoleDTO;
import com.example.book_web.entity.Role;
import com.example.book_web.request.role.RoleRequest;
import com.example.book_web.response.RoleDetailResponse;
import com.example.book_web.response.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleDTO createRole(RoleRequest request) ;
    RoleDetailResponse getRoleDetail(Long id);
    RoleDTO updateRole(Long id,RoleRequest request) ;
    void deleteRole(Long id);
}
