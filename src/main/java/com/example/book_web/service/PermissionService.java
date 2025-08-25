package com.example.book_web.service;

import com.example.book_web.dto.permission.PermissionDTO;
import com.example.book_web.entity.Permission;
import com.example.book_web.request.permission.PermissionRequest;

import java.util.List;

public interface PermissionService {
    PermissionDTO createPermission(PermissionRequest request) ;
    PermissionDTO updatePermission(Long id, PermissionRequest request) ;
    void deletePermission(Long id);
    List<PermissionDTO> getAllPermission();
    List<String> getDetailPermission(Long id) ;
}
