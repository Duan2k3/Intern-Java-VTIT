package com.example.book_web.service;

import com.example.book_web.dto.PermissionDTO;
import com.example.book_web.entity.Permission;

import java.util.List;

public interface PermissionService {
    Permission createPermission(PermissionDTO permissionDTO) throws Exception;
    Permission updatePermission( PermissionDTO permissionDTO) throws Exception;
    void deletePermission(Long id);
    List<PermissionDTO> getAllPermission();
    List<String> getDetailPermission(Long id) throws Exception;
}
