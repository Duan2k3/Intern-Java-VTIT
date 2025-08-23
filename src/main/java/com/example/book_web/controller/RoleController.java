package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.dto.role.RoleDTO;
import com.example.book_web.entity.Role;
import com.example.book_web.response.RoleDetailResponse;
import com.example.book_web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLE_GROUP')")
    public ResponseEntity<?> getAllRoles() {
        return ResponseConfig.success(roleService.getAllRoles());
    }

    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_ROLE_GROUP')")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO role) {
            return ResponseConfig.success(roleService.createRole(role),"Thanh cong");

    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLE_GROUP')")
    public ResponseEntity<ResponseDto<RoleDetailResponse>> getRoleDetail(@PathVariable Long id) {
            return ResponseConfig.success(roleService.getRoleDetail(id),"Thanh cong");
    }
    @Transactional
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_ROLE_GROUP')")
    public ResponseEntity<ResponseDto<Role>> updateRole(@PathVariable Long id, @RequestBody RoleDTO role) {

           return ResponseConfig.success(roleService.updateRole(id,role), "Thanh cong");
    }


    @Transactional
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_ROLE_GROUP')")
    public ResponseEntity<ResponseDto<Role>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseConfig.success(null,"Xoa thanh cong");
    }

}