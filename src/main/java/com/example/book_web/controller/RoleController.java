package com.example.book_web.controller;

import com.example.book_web.dto.RoleDTO;
import com.example.book_web.entity.Role;
import com.example.book_web.response.RoleDetailResponse;
import com.example.book_web.response.RoleResponse;
import com.example.book_web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLE_GROUP')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_ROLE_GROUP')")
    public ResponseEntity<String> createRole(@RequestBody RoleDTO role) {
        try {
            roleService.createRole(role);
            return ResponseEntity.ok("Role created successfully!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLE_GROUP')")
    public ResponseEntity<RoleDetailResponse> getRoleDetail(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(roleService.getRoleDetail(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(RoleDetailResponse.builder()
                    .message(e.getMessage())
                    .build());
        }

    }
    @Transactional
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_ROLE_GROUP')")
    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestBody RoleDTO role) {
       try {
         roleService.updateRole(id, role);
           return ResponseEntity.ok("Role updated successfully!");
       }
        catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());

        }

    }


    @Transactional
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_ROLE_GROUP')")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role deleted successfully!");
    }

}