package com.example.book_web.service;

import com.example.book_web.dto.role.RoleDTO;
import com.example.book_web.entity.Role;
import com.example.book_web.response.RoleDetailResponse;
import com.example.book_web.response.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    Role createRole(RoleDTO roleDTO) ;
    RoleDetailResponse getRoleDetail(Long id);
    Role updateRole(Long id, RoleDTO roleDTO) ;
    void deleteRole(Long id);
}
