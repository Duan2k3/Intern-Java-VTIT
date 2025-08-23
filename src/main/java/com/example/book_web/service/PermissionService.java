package com.example.book_web.service;

import com.example.book_web.dto.permission.PermissionDTO;
import com.example.book_web.entity.Permission;

import java.util.List;

public interface PermissionService {
    Permission createPermission(PermissionDTO permissionDTO) ;
    Permission updatePermission(Long id, PermissionDTO permissionDTO) ;
    void deletePermission(Long id);
    List<PermissionDTO> getAllPermission();
    List<String> getDetailPermission(Long id) ;
}
